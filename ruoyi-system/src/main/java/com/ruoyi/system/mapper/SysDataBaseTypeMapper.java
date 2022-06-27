package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysDataBaseType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-25 21:01
 **/
@Repository
public interface SysDataBaseTypeMapper {

    public SysDataBaseType getDriver(int typeId);

    public List<SysDataBaseType> list();
}