package com.ruoyi.system.service;


import com.ruoyi.system.domain.SysDataBaseType;

import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-25 20:34
 **/
public interface SysDataBaseTypeService {

    public SysDataBaseType getDriver(int typeId);

    public List<SysDataBaseType> list();

}