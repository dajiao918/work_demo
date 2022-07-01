package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class OracleScheMetaVo {

    private String schemaName;
    private List<OracleTableVo> children;

}
