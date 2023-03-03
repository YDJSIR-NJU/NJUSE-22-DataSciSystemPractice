# FlinkToClickHouse

## 项目说明

本项目用于接受Kafka的消息并把对应的流式数据放进ClickHouse（全文）和Elastic Search（摘要）。

## 运行说明

请使用IDEA直接打包，不要使用插件！

运行flink：

```bash
flink run ~/data/jar包名称
```

## 数据处理

​	我们将获取到的文本格式的流数据一行一行存放到txt文件中，然后通过kafka读取文件，并将数据按照topic进行推送，然后通过flink读取数据流，并按照不同的eventType进行分流处理，对应于每个不同的流根据数据库表的字段进行不同的sink操作。为了提高数据存入ClickHouse数据库的速度，引入了batch，不是每拿到一条数据就去建立一个数据库的连接进行存储，而是先缓存在batch里，之后在一次连接中将数据存入数据库，这样可以有效减少因不断与数据库建立连接产生的时间开销。需要注意的是，对于数据量不一样的数据需要设置不同的batch，这样才能保证数据的及时推送、并提升效率。除此之外，采用了多线程进行sink，由于不同的sink是存储到数据库中的不同表中，所以这样的并发并不会产生并发问题，设置并行度的上限为服务器的核心数量，如果并行度高于核心数，则无法正常执行。

​	具体到代码，数据存储时，先开启FlinkSinkClickHouse，然后开启kafkaproducer，模拟流数据推送，fink接收数据并处理。关于正确性验证，我们用一个另外的kafka消费者去统计一定量的流数据中不同类型数据的数量，然后与数据库中存入的数量进行对比。

Flink流程：

获取源数据：

```java
FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);
// 添加source数据流
DataStreamSource<String> source = env.addSource(consumer);
```

源数据分流：

```java
DataStream<String> sa_mx = source.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                HashMap<String, String> event = JSON.parseObject(s, HashMap.class);
                String eventType = event.get("eventType");
                return eventType.equals("sa");
            }
        }).setParallelism(Constant.GLOBAL_PARALLELISM);
```

sink处理、利用hashmap传数据：

```java
SingleOutputStreamOperator<HashMap<String, String>> sa_dataStream = sa_mx.map(new MapFunction<String, HashMap<String, String>>() {
            @Override
            public HashMap<String, String> map(String value) throws Exception {
                HashMap<String, String> event = JSON.parseObject(value, HashMap.class);
                String eventType = event.get("eventType");

                String eventDataStr = value.substring(1, value.lastIndexOf("}"));
                eventDataStr = eventDataStr.substring(eventDataStr.indexOf("{"));
                HashMap<String, String> eventData = JSON.parseObject(eventDataStr, HashMap.class);
                return eventData;
            }
        }).setParallelism(Constant.GLOBAL_PARALLELISM);

MyCkUtil_Sa _saUtil = new MyCkUtil_Sa();
sa_dataStream.addSink(_saUtil).setParallelism(Constant.GLOBAL_PARALLELISM);
```

