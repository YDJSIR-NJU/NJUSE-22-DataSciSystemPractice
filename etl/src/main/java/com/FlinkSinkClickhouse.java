package com;

import com.alibaba.fastjson.JSON;
import com.util.Constant;
import com.util.LMS_CH_Util;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;

import java.util.*;

import static com.util.Constant.*;

//不要和服务器一起跑、provided...
public class FlinkSinkClickhouse {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(5000);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        /**
         * 下方是输入流列表。本项目的输入有且只有Kafka消息队列
         */
        Properties props = new Properties();
        // 设置连接kafka集群的参数
        props.setProperty("bootstrap.servers", KAFKA_URL);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "191250186");

        System.out.println("-------------------------");

        // 定义Flink Kafka Consumer
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(KAFKA_TOPIC, new SimpleStringSchema(), props);

        // consumer.setStartFromGroupOffsets(); // 从对应GROUP_ID的偏移处开始处理
//        consumer.setStartFromEarliest();    // 设置每次都从头消费
        consumer.setStartFromLatest();      // 从开启推送之后的输入开始消费

        // 添加source数据流
        DataStreamSource<String> source = env.addSource(consumer);
        source.setParallelism(1);

        /**
         * 下方是数据流列表
         **/

        // 给标签打上UUID
        DataStream<HashMap<String, Object>> lms_tagged = source.map(new MapFunction<String, HashMap<String, Object>>() {
            @Override
            public HashMap<String, Object> map(String value) throws Exception {
                HashMap<String, Object> eventData = JSON.parseObject(value, HashMap.class);
                eventData.remove("@timestamp"); // 扔掉fluentbit加上的没用的秒级时间戳
                eventData.put("uuid", UUID.randomUUID().toString()); // 给每个消息打上独一无二的ID
                return eventData;
            }
        }).setParallelism(GLOBAL_PARALLELISM);

        // 筛选出一定级别以上的日志才进入ES供检索
        DataStream<HashMap<String, Object>> lms_prioritized = lms_tagged.filter(new FilterFunction<HashMap<String, Object>>() {
                @Override
                public boolean filter(HashMap<String, Object> stringObjectHashMap) throws Exception {
//                    return true;
                    String level = (String) stringObjectHashMap.get("level");
                    if(level == null){ // 记得校验空值
                        return false;
                    }
                    if (level.equals("WARN")) {
                        return true;
                    } else return level.equals("ERROR");
                }
            }
        ).setParallelism(GLOBAL_PARALLELISM);

        DataStream<String> lms_ESSinkNormalString = lms_prioritized.map(new MapFunction<HashMap<String, Object>, String>() {
            @Override
            public String map(HashMap<String, Object> value) throws Exception {
                value.remove("log");
                Long timestamp = (Long) value.get("log_timestamp");
                value.put("@timestamp", timestamp);
                value.remove("log_timestamp");
                String res = JSON.toJSONString(value);
                return res;
            }
        }).setParallelism(GLOBAL_PARALLELISM);

        /**
         * 下方是Sink输出列表
         */

        /** ClickHouse Sink **/
        LMS_CH_Util lms_CH_util = new LMS_CH_Util();
        lms_tagged.addSink(lms_CH_util).name(LMS_CH).setParallelism(GLOBAL_PARALLELISM);

        /**
         * Elasticsearch Sink
         */
        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost(HOSTNAME, 9200, "http"));

        ElasticsearchSink.Builder<String> esSinkBuilder = new ElasticsearchSink.Builder<String>(
                httpHosts,
                new ElasticsearchSinkFunction<String>() {
                    public IndexRequest createIndexRequest(String element) {
                        Map<String, Object> json = JSON.parseObject(element, HashMap.class);
                        return Requests.indexRequest()
                                .index("pri_lms")
                                .type("lms_log")
                                .source(json);
                    }

                    @Override
                    public void process(String element, RuntimeContext ctx, RequestIndexer indexer) {
                        indexer.add(createIndexRequest(element));
                    }
                }
        );
        esSinkBuilder.setFailureHandler(new ActionRequestFailureHandler() {
            @Override
            public void onFailure(ActionRequest action,
                                  Throwable failure,
                                  int restStatusCode,
                                  RequestIndexer indexer) throws Throwable {

                if (ExceptionUtils.findThrowable(failure, EsRejectedExecutionException.class).isPresent()) {
                    // full queue; re-add document for indexing
                    indexer.add(action);
                } else if (ExceptionUtils.findThrowable(failure, ElasticsearchParseException.class).isPresent()) {
                    // malformed document; simply drop request without failing sink
                } else {
                    // for all other failures, fail the sink
                    // here the failure is simply rethrown, but users can also choose to throw custom exceptions
                    throw failure;
                }
            }
        });
        esSinkBuilder.setBulkFlushMaxActions(INSERT_BATCH_SIZE); // 设置批处理的量

        lms_ESSinkNormalString.addSink(esSinkBuilder.build()).name(LMS_ES).setParallelism(GLOBAL_PARALLELISM);

        /**
         * 标准输出Sink，用于监视
         */
//        lms_tagged.print(LMS_CH).name(LMS_CH);
//        lms_ESSinkNormalString.print(LMS_ES).name(LMS_ES);

        System.out.println("DataStream print start:");
        System.out.println(env.getExecutionPlan());

        env.execute();
    }

    private static IndexRequest createIndexRequest(String element) {
        Map<String, Object> json = new HashMap<>();
        json.put("data", element);

        return Requests.indexRequest()
                .index("my-index")
                .id(element)
                .source(json);
    }
}
