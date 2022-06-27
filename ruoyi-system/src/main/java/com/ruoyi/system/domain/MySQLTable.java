package com.ruoyi.system.domain;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * @author: Mr.Yu
 * @create: 2022-06-25 11:23
 **/
@Data
public class MySQLTable {

    private String tableCatalog;
    private String tableSchema;
    private String schemaName;
    private String tableType;
    private String engine;
    private Long version;
    private String rowFormat;
    private Long tableRows;
    private Long avgRowLength;
    private Long dataLength;
    private Long maxDataLength;
    private Long indexLength;
    private Long dataFree;
    private Long autoIncrement;
    private Date createTime;
    private Date updateTime;
    private Date checkTime;
    private String tableCollation;
    private Long checksum;
    private String createOptions;
    private String tableComment;


}
