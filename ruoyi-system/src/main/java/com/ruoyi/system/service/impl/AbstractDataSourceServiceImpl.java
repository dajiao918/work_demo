package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DataSourceBuilder;
import com.ruoyi.common.utils.DataSourceFactory;
import com.ruoyi.system.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.system.datasource.DynamicDataSourceMap;
import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import com.ruoyi.system.mapper.SysDataSourceConfigMapper;
import com.ruoyi.system.mapper.SysOracleMapper;
import com.ruoyi.system.service.SysDataBaseTypeService;
import com.ruoyi.system.service.SysDataSourceService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-27 11:33
 **/
@Slf4j
public abstract class AbstractDataSourceServiceImpl implements SysDataSourceService {

    @Autowired
    protected SysDataSourceConfigMapper sysDataSourceConfigMapper;
    @Autowired
    protected SysDataBaseTypeService sysDataBaseTypeService;

    /**
     * 测试连接
     *  1. 创建数据源
     *  2. 尝试使用数据源获取连接
     * @param config
     * @param service 是否是controller直接调用，若是controller调用就不用再AjaxResult中存储DataSource；否则是service调用，返回数据源
     * @return
     */
    @Override
    public AjaxResult testConnection(SysDataSourceConfig config, boolean service) {
        Boolean result = (Boolean) dataSourceCommon(config, true);
        if (result) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    public Object dataSourceCommon(SysDataSourceConfig config, boolean service) {
        SysDataBaseType driver = sysDataBaseTypeService.getDriver(config.getType());
        DataSource dataSource =
                DataSourceFactory.createDataSource(driver.getDriver(),
                        config.getUrl(), config.getPassword(), config.getUsername(), config.getParamsMap());
        if (dataSource == null) {
            return false;
        }
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (Exception e) {
            return false;
        }
        if (service) {
            return true;
        }
        return dataSource;
    }

    /**
     * 删除连接两个步骤
     *  1. 删除数据库的记录信息
     *  2. 删除map中存储的DataSource
     * @param configId
     * @return
     */
    @Override
    public AjaxResult delDataSource(Long configId) {
        try {
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
            SysDataSourceConfig config = sysDataSourceConfigMapper.getDataSourceConfig(configId);
            String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
            log.info("删除前map size={}",
                    DynamicDataSourceMap.size());
            DynamicDataSourceMap.clearDataSource(key);
            log.info("删除后map size={}",
                    DynamicDataSourceMap.size());
            sysDataSourceConfigMapper.delete(configId);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * 查询数据库中的历史记录
     * @param userId
     * @return
     */
    @Override
    public AjaxResult history(Long userId) {

        List<SysDataSourceConfig> configs = sysDataSourceConfigMapper.histories(userId);
        return AjaxResult.success(configs);
    }


    @Override
    public AjaxResult connDataBase(SysDataSourceConfig config) {
        Object obj = dataSourceCommon(config, false);
        // 连接失败
        if (obj instanceof Boolean) {
            return AjaxResult.error("连接失败，请检查参数信息");
        }

        // 记录参数信息
        if (config.getParamsMap() != null && config.getParamsMap().size() != 0) {
            config.setParams(JSON.toJSONString(config.getParamsMap()));
        }
        config.setDisabled(false);
        config.setUpdateTime(new Date());
        boolean isNew = true;
        // 设置数据源为主数据库
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        // 重新连接
        if (config.getConfigId() != null) {
            // 判断id是否可以查询到数据
            SysDataSourceConfig sysDataSourceConfig = sysDataSourceConfigMapper.getDataSourceConfig(config.getConfigId());
            if (sysDataSourceConfig != null) {
                sysDataSourceConfigMapper.update(config);
                // 同一个用户的账号可能在不同网页打开连接，这样就不必再次生成DataSource，直接从map中查找
                String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
                if (DynamicDataSourceMap.contains(key)) {
                    DynamicDataSourceContextHolder.setDataSourceType(key);
                    doGetDataBases(config);
                    return AjaxResult.success(config);
                }
                isNew = false;
            }
        }
        // isNew为TRUE代表是新建连接
        if (isNew){
            config.setCreateTime(config.getUpdateTime());
            sysDataSourceConfigMapper.save(config);
        }

        // 获取数据源
        DataSource dataSource = (DataSource) obj;
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        log.info("数据源id--{}",key);
        // 存入map
        DynamicDataSourceMap.setDatasourceMap(key,dataSource);
        // 动态获取数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        // 调用具体类的方法获取数据库信息
        doGetDataBases(config);
        return AjaxResult.success(config);
    }

    /**
     * 关闭连接两个步骤
     *  1. 设置数据库中信息，连接关闭+
     *  2. 删除map中存储的DataSource
     * @param configId
     * @return
     */
    @Override
    public AjaxResult closeConn(Long configId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        SysDataSourceConfig config = sysDataSourceConfigMapper.getDataSourceConfig(configId);
        config.setDisabled(true);
        sysDataSourceConfigMapper.update(config);
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        DynamicDataSourceMap.clearDataSource(key);
        return AjaxResult.success();
    }

    @Override
    public SysDataSourceConfig getDataSourceConfig(Long configId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        return sysDataSourceConfigMapper.getDataSourceConfig(configId);
    }

    @Override
    public AjaxResult addDataBase(SysDataSourceConfig config, DataBaseConfigVo dataBaseConfigVo) {
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        // 动态设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        return doCreateDataBase(dataBaseConfigVo);
    }

    @Override
    public AjaxResult delDataBase(SysDataSourceConfig config, String dataBaseName) {
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        // 动态设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        return doDelDataBase(dataBaseName);
    }

    @Override
    public AjaxResult updateDataBase(SysDataSourceConfig config, DataBaseConfigVo dataBaseConfigVo) {
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        // 动态设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        return doUpdateDataBase(dataBaseConfigVo);
    }

    @Override
    public AjaxResult getDataBase(SysDataSourceConfig config, String dataBaseName) {
        String key = getKey(config.getUserId(),config.getCreateTime().getTime(),null,true);
        // 动态设置数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        return doGetDataBase(dataBaseName);
    }

    protected String getKey(Long userId, Long time, String dataBaseName, boolean dataSource) {
        StringBuilder builder = new StringBuilder();
        // 初始数据源的key
        if (dataSource) {
            return builder.append(userId).append(time).toString();
        } else { // 某个具体数据库的key
            return builder.append(userId).append(time).append(dataBaseName).toString();
        }
    }

    @Override
    public AjaxResult addTable(SysDataSourceConfig config, String dataBaseName, Table table) {
        preDynamicDataSourceSet(config,dataBaseName);
        // 3. 调用子类的具体实现
        return doCreateTables(table,dataBaseName);
    }

    protected void preDynamicDataSourceSet(SysDataSourceConfig config, String dataBaseName) {
        // 1. 根据 userId+createTime+数据库名/用户名来生成key，判断map中是否有该数据库对应的DataSource：这里是否让数据库也默认关闭连接呢？
        String key = getKey(config.getUserId(), config.getCreateTime().getTime(), dataBaseName, false);
        if (!DynamicDataSourceMap.contains(key)) {
            SysDataBaseType driver = sysDataBaseTypeService.getDriver(config.getType());
            Map map = null;
            if (config.getParams() != null) {
                map = JSON.parseObject(config.getParams(), Map.class);
            }
            DataSourceBuilder builder = DataSourceBuilder.build()
                    .setDriver(driver.getDriver())
                    .setPassword(config.getPassword())
                    .setUsername(config.getUsername())
                    .setUrl(config.getUrl())
                    .setParamsMap(map)
                    .setSchema(dataBaseName);
            DataSource dataSource = DataSourceFactory.createDataSource(builder);
            DynamicDataSourceMap.setDatasourceMap(key,dataSource);
        }
        DynamicDataSourceContextHolder.setDataSourceType(key);
    }

    @Override
    public AjaxResult delTable(SysDataSourceConfig config, String dataBaseName, String tableName) {
        preDynamicDataSourceSet(config,dataBaseName);
        // 3. 调用子类的具体实现
        return doDelTable(tableName);
    }

    @Override
    public List<Map<String,Object>> getTableData(SysDataSourceConfig config, String dataBaseName, String tableName, Page<Map<String,Object>> page) {
        preDynamicDataSourceSet(config,dataBaseName);
        // 3. 调用子类的具体实现
        return doGetTable(tableName,page);
    }

    @Override
    public AjaxResult delTbData(SysDataSourceConfig config, String tableName, String dataBaseName, Map<String, Object> map) {
        preDynamicDataSourceSet(config,dataBaseName);
        return doDelTableData(tableName,map);
    }

    @Override
    public AjaxResult updateTbData(SysDataSourceConfig config, String tableName,
                                   String dataBaseName, Map<String, Map<String,Object>> map) {
        preDynamicDataSourceSet(config,dataBaseName);
        Map<String, Object> oldMap = map.get("oldMap");
        Map<String, Object> newMap = map.get("newMap");
        return doUpdateTableData(tableName,oldMap,newMap);
    }

    @Override
    public AjaxResult saveTbData(SysDataSourceConfig config, String tableName, String dataBaseName, Map<String, Object> map) {
        preDynamicDataSourceSet(config,dataBaseName);
        return doSaveTableData(tableName,map);
    }

    protected abstract AjaxResult doSaveTableData(String tableName, Map<String, Object> map);

    protected abstract AjaxResult doUpdateTableData(String tableName, Map<String, Object> oldMap, Map<String,Object> newMap);

    protected abstract AjaxResult doDelTableData(String tableName, Map<String, Object> map);

    protected abstract List<Map<String,Object>> doGetTable(String tableName, Page<Map<String,Object>> page);

    protected abstract AjaxResult doGetDataBase(String dataBaseName);

    protected abstract AjaxResult doDelTable(String tableName);

    protected abstract AjaxResult doCreateTables(Table table, String schemaName);

    protected abstract void doGetDataBases(SysDataSourceConfig config);

    protected abstract AjaxResult doUpdateDataBase(DataBaseConfigVo dataBaseConfigVo);

    protected abstract AjaxResult doDelDataBase(String dataBaseName);

    protected abstract AjaxResult doCreateDataBase(DataBaseConfigVo dataBaseConfigVo);


}
