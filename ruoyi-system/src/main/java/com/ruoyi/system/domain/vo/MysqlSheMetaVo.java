package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.vo.MySQLTableMetaVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 22:31
 **/
@Data
public class MysqlSheMetaVo {

    private String catalogName;
    private String schemaName;
    private String characterSetName;
    private String collationName;
    private String sqlPath;
    private List<MySQLTableMetaVo> children = new ArrayList<>();

}
