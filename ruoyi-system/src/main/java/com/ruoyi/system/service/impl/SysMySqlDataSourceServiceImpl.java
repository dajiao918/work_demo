package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DataSourceFactory;
import com.ruoyi.system.domain.DynamicDataSourceContextHolder;
import com.ruoyi.system.domain.DynamicDataSourceMap;
import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.service.SysDataSourceService;
import com.ruoyi.system.domain.MysqlSheMeta;
import com.ruoyi.system.domain.vo.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Date;
import java.util.List;


/**
 * @author: Mr.Yu
 * @create: 2022-06-24 22:35
 **/
@Service("mysqlDataSourceServiceImpl")
@Slf4j
public class SysMySqlDataSourceServiceImpl extends AbstractDataSourceServiceImpl implements SysDataSourceService {

    @Override
    public AjaxResult connDataBase( DataSourceConfig config) {
        long userId = config.getUserId();
        AjaxResult ajaxResult = testConnection(config, false);
        if (ajaxResult.getCode() != HttpStatus.SUCCESS) {
            return ajaxResult;
        }
        config.setCreateTime(new Date());
        config.setUpdateTime(config.getCreateTime());

        // 记录连接信息
        if (config.getParamsMap() != null && config.getParamsMap().size() != 0) {
            config.setParams(JSON.toJSONString(config.getParamsMap()));
        }
        // 设置数据源为当前数据库
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        sysDataSourceMapper.save(config);

        // 获取数据源
        DataSource dataSource = (DataSource) ajaxResult.get(AjaxResult.DATA_TAG);
        String key = userId + "" + config.getCreateTime().getTime();
        // 存入map
        DynamicDataSourceMap.setDatasourceMap(key,dataSource);
        // 动态获取数据源
        DynamicDataSourceContextHolder.setDataSourceType(key);
        List<MysqlSheMeta> sheMetas = sysDataSourceMapper.metaList();
        config.setChildren(sheMetas);
        return AjaxResult.success(config);
    }

    @Override
    public AjaxResult testConnection(DataSourceConfig config, boolean service) {
        SysDataBaseType driver = sysDataBaseTypeService.getDriver(config.getType());
        DataSource dataSource =
                DataSourceFactory.createDataSource(driver.getDriver(),
                config.getUrl(), config.getPassword(), config.getUsername(), config.getParamsMap());
        if (dataSource == null) {
            return AjaxResult.error("创建数据源失败！");
        }
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        if (service) {
            return AjaxResult.success();
        }
        return AjaxResult.success(dataSource);
    }

    @Override
    public AjaxResult getTables(DataSourceConfig config, String databaseName) {

        return null;
    }






}
