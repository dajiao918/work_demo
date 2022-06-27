package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DataSourceFactory;
import com.ruoyi.system.domain.DynamicDataSourceContextHolder;
import com.ruoyi.system.domain.DynamicDataSourceMap;
import com.ruoyi.system.domain.MysqlSheMeta;
import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.domain.vo.DataSourceConfig;
import com.ruoyi.system.mapper.SysDataSourceMapper;
import com.ruoyi.system.service.SysDataBaseTypeService;
import com.ruoyi.system.service.SysDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-27 11:33
 **/
@Service
@Slf4j
public abstract class AbstractDataSourceServiceImpl implements SysDataSourceService {

    @Autowired
    protected SysDataSourceMapper sysDataSourceMapper;
    @Autowired
    protected SysDataBaseTypeService sysDataBaseTypeService;

    @Override
    public AjaxResult delDataSource(Long configId) {
        try {
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
            DataSourceConfig config = sysDataSourceMapper.getDataSourceConfig(configId);
            String key = config.getUserId() + "" + config.getCreateTime().getTime();
            log.info("删除前map size={}",
                    DynamicDataSourceMap.size());
            DynamicDataSourceMap.clearDataSource(key);
            log.info("删除后map size={}",
                    DynamicDataSourceMap.size());
            sysDataSourceMapper.delete(configId);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }
    @Override
    public AjaxResult history(Long userId) {

        List<DataSourceConfig> configs = sysDataSourceMapper.histories(userId);
        StringBuilder builder = new StringBuilder();
        List<DataSourceConfig> allBases = new ArrayList<>();
        for (DataSourceConfig config : configs) {
            String params = config.getParams();
            if (params != null) {
                Map map = JSON.parseObject(params, Map.class);
                config.setParamsMap(map);
            }
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
            SysDataBaseType driver = sysDataBaseTypeService.getDriver(config.getType());
            DataSource dataSource =
                    DataSourceFactory.createDataSource(driver.getDriver(), config.getUrl(),
                            config.getPassword(), config.getUsername(), config.getParamsMap());
            String key = builder.append(config.getUserId())
                    .append(config.getCreateTime().getTime()).toString();
            builder = new StringBuilder();
            DynamicDataSourceMap.setDatasourceMap(key, dataSource);
            DynamicDataSourceContextHolder.setDataSourceType(key);

            // 不同类型对应不同策略
            //...
            List<MysqlSheMeta> mysqlSheMetas = sysDataSourceMapper.metaList();
            config.setChildren(mysqlSheMetas);
            allBases.add(config);
        }
        return AjaxResult.success(allBases);
    }
}
