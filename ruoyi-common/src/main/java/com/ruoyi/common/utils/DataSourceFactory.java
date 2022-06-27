package com.ruoyi.common.utils;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 22:48
 **/
public class DataSourceFactory {

    public static final String MYSQL_DEFAULT_TABLE = "information_schema";
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
    public static final String MYSQL_DEFAULT_PARAMS = "?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false";
    public static final String MYSQL_URL_SUBFIX =
            "/" + MYSQL_DEFAULT_TABLE + MYSQL_DEFAULT_PARAMS;



    public enum DataBaseType {
        MYSQL,
        ORACLE,
        DM
    }

    public static DataSource createDataSource(String driver, String ip, String password,
                                              String username, Map<String,String> params){
        if (driver == null || driver.contains("mysql")) {
            return createMySqlDataSource(driver,ip,
                    password,username,params);
        }
        else if (driver.contains("oracle")) {
            return createOracleDataSource(ip,
                    password,username,params);
        } else if (driver.contains("dm")) {
            return createDmDataSource(ip,
                    password,username,params);
        } else {
            return createMySqlDataSource(driver,ip,
                    password,username,params);
        }
    }

    public static DataSource createDataSource(DataBaseType type, String ip, String password,
                                              String username, Map<String,String> params) {

        switch (type) {
            case MYSQL:
                return createMySqlDataSource(MYSQL_DRIVER,ip,password, username,params);
            case ORACLE:
                return createOracleDataSource(ip,password,username,params);
            case DM:
                return createDmDataSource(ip,password,username,params);
        }
        return null;
    }

    private static DataSource createDmDataSource(String ip, String password, String username, Map<String,String> params) {
        return null;
    }

    private static DataSource createOracleDataSource(String ip, String password, String username, Map<String,String> params) {
        return null;
    }

    private static DataSource createMySqlDataSource(String driver, String ip, String password, String username, Map<String,String> params) {
        StringBuilder url = new StringBuilder().append(MYSQL_URL_PREFIX).append(ip);
        if (params == null || params.size() == 0) {
             url.append(MYSQL_URL_SUBFIX);
        } else {
            url.append("/").append(MYSQL_DEFAULT_TABLE).append("?");
            int size = params.size();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (size == 1) {
                    url.append(entry.getKey()).append("=").append(entry.getValue());
                    break;
                }
                url.append("&");
                size --;
            }
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url.toString());
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

}
