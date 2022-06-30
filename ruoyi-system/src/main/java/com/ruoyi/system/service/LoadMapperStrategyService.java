package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.domain.SysDataSourceConfig;

public interface LoadMapperStrategyService {

    public Object loadMapper(SysDataSourceConfig sysDataSourceConfig, SysDataBaseType driver);

}
