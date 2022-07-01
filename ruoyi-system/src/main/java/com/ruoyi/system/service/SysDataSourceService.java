package com.ruoyi.system.service;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;

import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 21:26
 **/
public interface SysDataSourceService {



    public AjaxResult connDataBase( SysDataSourceConfig sysDataSourceConfig);

    public AjaxResult testConnection(SysDataSourceConfig sysDataSourceConfig, boolean service);

    AjaxResult getTables(SysDataSourceConfig config, String databaseName);

    AjaxResult delDataSource(Long configId);

    AjaxResult history(Long userId);

    AjaxResult closeConn(Long configId);

    AjaxResult addDataBase(SysDataSourceConfig config, DataBaseConfigVo dataBaseConfigVo);

    SysDataSourceConfig getDataSourceConfig(Long configId);

    AjaxResult addTable(SysDataSourceConfig config, String dataBaseName, Table table);

    AjaxResult delDataBase(SysDataSourceConfig sysDataSourceConfig, String dataBaseName);

    AjaxResult updateDataBase(SysDataSourceConfig sysDataSourceConfig, DataBaseConfigVo dataBaseConfigVo);

    AjaxResult delTable(SysDataSourceConfig sysDataSourceConfig, String dataBaseName, String tableName);

    AjaxResult getDataBase(SysDataSourceConfig config, String dataBaseName);

    List<Map<String,Object>> getTableData(SysDataSourceConfig config, String dataBaseName, String tableName, Page<Map<String,Object>> page);

    AjaxResult delTbData(SysDataSourceConfig sysDataSourceConfig, String tableName, String dataBaseName, Map<String, Object> map);

    AjaxResult saveTbData(SysDataSourceConfig sysDataSourceConfig, String tableName, String dataBaseName, Map<String, Object> map);

    AjaxResult updateTbData(SysDataSourceConfig sysDataSourceConfig, String tableName, String dataBaseName, Map<String, Map<String,Object>> map);


}