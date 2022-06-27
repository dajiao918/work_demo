package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.DataSourceConfig;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 21:26
 **/
public interface SysMySQLDataSourceService {



    public AjaxResult connDataBase( DataSourceConfig dataSourceConfig);

    public AjaxResult testConnection(DataSourceConfig dataSourceConfig, boolean service);

    AjaxResult getTables(DataSourceConfig config, String databaseName);

    AjaxResult delDataSource(Long configId);

    AjaxResult history(Long userId);
}