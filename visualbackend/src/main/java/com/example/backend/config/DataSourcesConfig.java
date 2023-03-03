package com.example.backend.config;

import cc.blynk.clickhouse.ClickHouseDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourcesConfig {

    @Bean(name = "clickHouseDataSource")
    public DataSource clickHouseDataSource(@Qualifier("clickHouseDataSourceProperties") ClickHouseDataSourceProperties clickHouseDataSourceProperties) {
        return new ClickHouseDataSource(clickHouseDataSourceProperties.getUrl(), clickHouseDataSourceProperties);
    }

    @Bean(name = "clickHouseDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public ClickHouseDataSourceProperties clickHouseDataSourceProperties() {
        return new ClickHouseDataSourceProperties();
    }
}
