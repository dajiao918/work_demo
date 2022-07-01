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
    public static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
    public static final String MYSQL_DEFAULT_PARAMS = "?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false";
    public static final String ORACLE_PREFIX = "jdbc:oracle:thin:@";
    public static final String ORACLE_SUBFIX = ":1521:ORCL";



    public enum DataBaseType {
        MYSQL,
        ORACLE,
        DM
    }

    public static DataSource createDataSource(String driver, String ip, String password,
                                              String username, Map<String,String> params){
        return createDataSource(driver,ip,password,username,params,null);
    }

    public static DataSource createDataSource(String driver, String ip, String password,
                                              String username, Map<String,String> params, String schemaName){
        if (driver == null || driver.contains("mysql")) {
            return createMySqlDataSource(driver,ip,
                    password,username,params, schemaName);
        }
        else if (driver.contains("oracle")) {
            return createOracleDataSource(ip,
                    password,username,params, schemaName);
        } else if (driver.contains("dm")) {
            return createDmDataSource(ip,
                    password,username,params, schemaName);
        } else {
            return createMySqlDataSource(driver,ip,
                    password,username,params, schemaName);
        }
    }

    public static DataSource createDataSource(DataSourceBuilder builder) {
        return createDataSource(builder.getDriver(),builder.getUrl(),builder.getPassword(),builder.getUsername(),builder.getParamsMap(),builder.getSchema());
    }

    private static DataSource createDmDataSource(String ip, String password, String username, Map<String,String> params, String schemaName) {
        return null;
    }

    private static DataSource createOracleDataSource(String ip, String password, String username, Map<String,String> params, String schemaName) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setJdbcUrl(ORACLE_PREFIX + ip + ORACLE_SUBFIX);
        return dataSource;
    }

    private static DataSource createMySqlDataSource(String driver, String ip, String password, String username, Map<String,String> params, String databaseName) {
        databaseName = databaseName == null ? MYSQL_DEFAULT_TABLE : databaseName;
        StringBuilder url = new StringBuilder().append(MYSQL_URL_PREFIX).append(ip);
        if (params == null || params.size() == 0) {
            url.append("/").append(databaseName).append(MYSQL_DEFAULT_PARAMS);
        } else {
            url.append("/").append(databaseName).append("?");
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
