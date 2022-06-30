package com.ruoyi.web.controller.strategy;

import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.service.SysDataSourceService;

/**
 * @author: Mr.Yu
 * @create: 2022-06-27 11:20
 **/
public interface LoadDataSourceStrategy {

    public SysDataSourceService getInstance(SysDataSourceConfig config);

    SysDataSourceService getInstance(Long configId);
}