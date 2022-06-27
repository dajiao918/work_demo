package com.ruoyi.system.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 22:31
 **/
@Data
public class MysqlSheMeta {

    private String catalogName;
    private String schemaName;
    private String defaultCharacterSetName;
    private String defaultCollationName;
    private String sqlPath;
    private List<MySQLTable> children = new ArrayList<>();

}
