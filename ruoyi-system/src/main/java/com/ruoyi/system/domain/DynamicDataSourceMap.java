package com.ruoyi.system.domain;

import javax.sql.DataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 21:06
 **/
public class DynamicDataSourceMap {

    public static final Map<Object, Object> DATASOURCE_MAP = new ConcurrentHashMap<>();

    public static void setDatasourceMap(String key, DataSource dataSource) {
        DATASOURCE_MAP.put(key, dataSource);
    }

    public static DataSource getDataSource(String key) {
       return (DataSource) DATASOURCE_MAP.get(key);
    }

    public static void clearDataSource(String key) {
        DATASOURCE_MAP.remove(key);
    }

    public static void clear() {
        DATASOURCE_MAP.clear();
    }

    public static int size() {
        return DATASOURCE_MAP.size();
    }
}
