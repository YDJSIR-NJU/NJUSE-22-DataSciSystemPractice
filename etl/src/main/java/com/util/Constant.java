package com.util;

public class Constant {
    public static final int INSERT_BATCH_SIZE = 100;
    public static final int GLOBAL_PARALLELISM = 4; // 本地调试的最大值设为cpu最大核心数、否则无法运行
    public static final int INSERT_LOG_SIZE = 100;
    public static final String LMS_CH = "LMS_CH";
    public static final String LMS_ES = "LMS_ES";
    public static final String HOSTNAME = "10.168.0.23";
    public static final String KAFKA_TOPIC = "mx";
    public static final String KAFKA_URL = HOSTNAME + ":9092";
    public static final String DATABASE_NAME = "default";
    public static final String DATABASE_URL = "jdbc:clickhouse://" + HOSTNAME + ":8123/" + DATABASE_NAME;
}
