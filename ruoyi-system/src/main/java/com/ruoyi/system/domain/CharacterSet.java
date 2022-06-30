package com.ruoyi.system.domain;

import lombok.Data;

import java.util.List;

@Data
public class CharacterSet {

    private String characterSetName;
    private String defaultCollateName;
    private List<String> collationNames;

}
