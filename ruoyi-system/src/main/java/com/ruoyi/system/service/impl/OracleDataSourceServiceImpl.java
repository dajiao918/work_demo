package com.ruoyi.system.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import com.ruoyi.system.domain.vo.OracleScheMetaVo;
import com.ruoyi.system.mapper.SysOracleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OracleDataSourceServiceImpl extends AbstractDataSourceServiceImpl{

    @Autowired
    private SysOracleMapper sysOracleMapper;

    @Override
    public AjaxResult getTables(SysDataSourceConfig config, String databaseName) {
        return null;
    }

    @Override
    protected AjaxResult doDelTableData(String tableName, Map<String, Object> map) {
        return null;
    }

    @Override
    protected List<Map<String, Object>> doGetTable(String tableName, Page<Map<String, Object>> page) {
        return null;
    }

    @Override
    protected AjaxResult doGetDataBase(String dataBaseName) {
        return null;
    }

    @Override
    protected AjaxResult doDelTable(String tableName) {
        return null;
    }

    @Override
    protected AjaxResult doCreateTables(Table table, String schemaName) {
        return null;
    }

    @Override
    protected void doGetDataBases(SysDataSourceConfig config) {
        List<OracleScheMetaVo> oracleScheMetaVos = sysOracleMapper.getDataBases();
        config.setChildren(oracleScheMetaVos);
    }

    @Override
    protected AjaxResult doUpdateDataBase(DataBaseConfigVo dataBaseConfigVo) {
        return null;
    }

    @Override
    protected AjaxResult doDelDataBase(String dataBaseName) {
        return null;
    }

    @Override
    protected AjaxResult doCreateDataBase(DataBaseConfigVo dataBaseConfigVo) {
        sysOracleMapper.createDataBase(dataBaseConfigVo);
        return AjaxResult.success();
    }

    @Override
    protected AjaxResult doUpdateTableData(String tableName, Map<String, Object> oldMap, Map<String,Object> newMap) {
        return null;
    }

    @Override
    protected AjaxResult doSaveTableData(String tableName, Map<String, Object> map) {
        return null;
    }
}
