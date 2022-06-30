package com.ruoyi.common.utils;

import java.util.Map;

public class DataSourceBuilder {

    private String driver;
    private String url;
    private String password;
    private String username;
    private String schema;
    private Map<String,String> paramsMap;

    public static DataSourceBuilder build(){
        return new DataSourceBuilder();
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getSchema() {
        return schema;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public DataSourceBuilder setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public DataSourceBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public DataSourceBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public DataSourceBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public DataSourceBuilder setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public DataSourceBuilder setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
        return this;
    }
}
