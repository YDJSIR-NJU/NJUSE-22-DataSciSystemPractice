package com.example.backend.config;


import cc.blynk.clickhouse.settings.ClickHouseProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickHouseDataSourceProperties extends ClickHouseProperties {
    private String url;
}
