package com.ruoyi.system.domain;


import lombok.Data;

@Data
public class Field {

    private String fieldName;
    private String fieldType;
    private String filedLength;
    private String constrict;
    private String defaultValue;
    private Boolean isNotNull;
    private String comment;
    private Boolean isPrimaryKey;

}
