package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 21:27
 **/
@Data
public class DataSourceConfig {

    private Long configId;
    private Long userId;
    private Integer type;
    private String schemaName;
    private String url;
    private String username;
    private String password;
    private Date createTime;
    private Date updateTime;
    private String params;
    private Map<String,String> paramsMap;
    private Object children;

}
