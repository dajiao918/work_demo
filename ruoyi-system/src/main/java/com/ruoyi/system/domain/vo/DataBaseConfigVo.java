package com.ruoyi.system.domain.vo;

import lombok.Data;

@Data
public class DataBaseConfigVo {

    private String schemaName;
    private String characterSetName;
    private String collationName;
    private String password; // oracle创建用户，需要密码

}
