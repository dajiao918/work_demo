package com.ruoyi.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import com.ruoyi.system.domain.vo.MySQLTableMetaVo;
import com.ruoyi.system.mapper.SysMySQLMapper;
import com.ruoyi.system.service.SysDataSourceService;
import com.ruoyi.system.domain.vo.MysqlSheMetaVo;
import com.ruoyi.system.domain.SysDataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author: Mr.Yu
 * @create: 2022-06-24 22:35
 **/
@Service("mysqlDataSourceServiceImpl")
@Slf4j
public class SysMySqlDataSourceServiceImpl extends AbstractDataSourceServiceImpl implements SysDataSourceService {

    @Autowired
    protected SysMySQLMapper sysMySQLMapper;

    @Override
    protected void doGetDataBases(SysDataSourceConfig config) {
        List<MysqlSheMetaVo> sheMetas = sysMySQLMapper.metaList();
        config.setChildren(sheMetas);
    }

    @Override
    public AjaxResult getTables(SysDataSourceConfig config, String databaseName) {
        return null;
    }

    @Override
    protected AjaxResult doUpdateDataBase(DataBaseConfigVo dataBaseConfigVo) {
        sysMySQLMapper.updateDataBase(dataBaseConfigVo);
        return AjaxResult.success();
    }

    @Override
    protected AjaxResult doCreateDataBase(DataBaseConfigVo dataBaseConfigVo) {

        // 判断数据库是否存在
        List<MysqlSheMetaVo> sheMetas = sysMySQLMapper.metaList();
        for (MysqlSheMetaVo sheMeta : sheMetas) {
            if (sheMeta.getSchemaName().equals(dataBaseConfigVo.getSchemaName())) {
                return AjaxResult.error("数据库已存在!");
            }
        }
        // 创建数据库
        sysMySQLMapper.createDatabase(dataBaseConfigVo);
        // 返回mysql数据库对象
        MysqlSheMetaVo mysqlSheMetaVo = new MysqlSheMetaVo();
        mysqlSheMetaVo.setSchemaName(dataBaseConfigVo.getSchemaName());
        mysqlSheMetaVo.setCharacterSetName(dataBaseConfigVo.getCharacterSetName());
        mysqlSheMetaVo.setCollationName(dataBaseConfigVo.getCollationName());
        return AjaxResult.success(mysqlSheMetaVo);
    }

    @Override
    protected AjaxResult doDelDataBase(String dataBaseName) {
        sysMySQLMapper.delDataBase(dataBaseName);
        return AjaxResult.success();
    }

    @Override
    protected AjaxResult doCreateTables(Table table, String databaseName) {

        List<MySQLTableMetaVo> tables = sysMySQLMapper.getTables(databaseName);
        for (MySQLTableMetaVo tableMetaVo : tables) {
            if (tableMetaVo.getSchemaName().equals(table.getTableName())) {
                return AjaxResult.error("表已经存在！");
            }
        }

        sysMySQLMapper.createTable(table);
        // 返回表信息
        MySQLTableMetaVo resTable = sysMySQLMapper.getTable(databaseName, table.getTableName());
        return AjaxResult.success(resTable);
    }

    @Override
    protected AjaxResult doDelTable(String tableName) {
        sysMySQLMapper.delTable(tableName);
        return AjaxResult.success();
    }

    @Override
    protected List<Map<String,Object>> doGetTable(String tableName, Page<Map<String,Object>> page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        return sysMySQLMapper.getTableData(tableName);
    }
}
