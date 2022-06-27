package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.mapper.SysDataBaseTypeMapper;
import com.ruoyi.system.service.SysDataBaseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-25 21:00
 **/
@Service
public class SysDataBaseTypeServiceImpl implements SysDataBaseTypeService {

    @Autowired
    SysDataBaseTypeMapper sysDataBaseTypeMapper;

    @Override
    public SysDataBaseType getDriver(int typeId) {
        return sysDataBaseTypeMapper.getDriver(typeId);
    }

    @Override
    public List<SysDataBaseType> list() {
        return sysDataBaseTypeMapper.list();
    }


}
