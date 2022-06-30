package com.ruoyi.system.domain;

import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String tableName;
    private String characterSetName;
    private String collateName;
    private String engine;
    private List<Field> fields;

}
