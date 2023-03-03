package com.util;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import ru.yandex.clickhouse.settings.ClickHouseQueryParam;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LMS_CH_Util extends RichSinkFunction<HashMap<String, Object>> {
    String sql;
    PreparedStatement preparedStatement;//放到成员变量，否则会被清空
    private ClickHouseConnection conn = null;
    private int count = 0;

    public LMS_CH_Util() {
        this.sql = "INSERT INTO " + Constant.DATABASE_NAME + ".logManagementSystem (timestamp, date, level, appName, " +
                "thread, class, line, msg, stack_trace, log, container_id, container_name, source, uuid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        return;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public void invoke(HashMap<String, Object> eventData, Context context) throws Exception {

        String url = Constant.DATABASE_URL;
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser("default");
        properties.setPassword("");
        String uuid = String.valueOf(UUID.randomUUID());
        properties.setSessionId(uuid);

        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);
        Map<ClickHouseQueryParam, String> additionalDBParams = new HashMap<>();

        additionalDBParams.put(ClickHouseQueryParam.SESSION_ID, uuid);

        Long timestamp = (Long) eventData.getOrDefault("log_timestamp", 0L);
        String level = (String) eventData.getOrDefault("level", "NULL");
        String appName = (String) eventData.getOrDefault("appName", "NULL");
        String thread = (String) eventData.getOrDefault("thread", "NULL");
        String className = (String) eventData.getOrDefault("class", "NULL");
        String line = (String) eventData.getOrDefault("line", "NULL");
        String msg = (String) eventData.getOrDefault("msg", "NULL");
        String stack_trace = (String) eventData.getOrDefault("stack_trace", "NULL");
        String log = (String) eventData.getOrDefault("log", "NULL");
        String container_id = (String) eventData.getOrDefault("container_id", "NULL");
        String container_name = (String) eventData.getOrDefault("container_name", "NULL");
        String source = (String) eventData.getOrDefault("source", "NULL");
        String uuidName = (String) eventData.getOrDefault("uuid", "NULL");

        try {

            if (conn == null) {
                conn = dataSource.getConnection();
                //优化：设置为手动提交，否则插入一次就会提交一次
                conn.setAutoCommit(false);
                preparedStatement = conn.prepareStatement(sql);
            }
        } catch (Exception e) {
//            preparedStatement.clearBatch();
//            System.err.println(eventData.get("log"));
            e.printStackTrace();
//            Thread.sleep(10000);
        }

        preparedStatement.setLong(1, 0L);
        preparedStatement.setLong(1, timestamp);
        preparedStatement.setTimestamp(2, new java.sql.Timestamp(timestamp));
        preparedStatement.setString(3, level);
        preparedStatement.setString(4, appName);
        preparedStatement.setString(5, thread);
        preparedStatement.setString(6, className);
        preparedStatement.setString(7, line);
        preparedStatement.setString(8, msg);
        preparedStatement.setString(9, stack_trace);
        preparedStatement.setString(10, log);
        preparedStatement.setString(11, container_id);
        preparedStatement.setString(12, container_name);
        preparedStatement.setString(13, source);
        preparedStatement.setString(14, uuidName);

//            preparedStatement.execute();

//            ParameterMetaData metadata = preparedStatement.getParameterMetaData();
//            int pcount = metadata.getParameterCount();
        //插入代码打包，等一定量后再一起插入。

        count += 1;

        try {
            preparedStatement.addBatch();

            int[] successLines;
            if (count != 0 && count % Constant.INSERT_BATCH_SIZE == 0) {//最后几条可能丢弃，对于流数据，这是合理的
                successLines = preparedStatement.executeBatch();
                //优化插入第三步       提交，批量插入数据库中。
                conn.commit();
                preparedStatement.clearBatch();
                System.out.println("default.logManagementSystem: " + count + " items inserted, " + successLines.length + " successfully inserted at this time");
//                count = 0;
            } else {
//                System.out.print(count + " ");
            }
        } catch (Exception e) {
//            preparedStatement.clearBatch();
//            System.err.println(eventData.get("log"));
            e.printStackTrace();
//            Thread.sleep(10000);
        }
    }
}
