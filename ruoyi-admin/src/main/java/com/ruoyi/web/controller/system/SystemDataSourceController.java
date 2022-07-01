package com.ruoyi.web.controller.system;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import com.ruoyi.system.service.SysDataSourceService;
import com.ruoyi.web.controller.strategy.LoadDataSourceStrategy;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 23:16
 **/
@Slf4j
@RestController
@RequestMapping("/system/dataSource")
public class SystemDataSourceController {

    @Qualifier("mysqlDataSourceServiceImpl")
    @Autowired
    SysDataSourceService sysDataSourceService;
    @Autowired
    LoadDataSourceStrategy loadDataSourceStrategy;

    @ApiOperation("历史连接记录查询")
    @GetMapping("/history")
    public AjaxResult historyDatabases() {
        return sysDataSourceService.history(SecurityUtils.getUserId());
    }

    @ApiOperation("连接数据库，获取数据库信息接口")
    @PostMapping("/databases")
    public AjaxResult connDatabases(@RequestBody SysDataSourceConfig sysDataSourceConfig) {
        sysDataSourceConfig.setUserId(SecurityUtils.getUserId());
        SysDataSourceService sysDataSourceService =
                loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        AjaxResult ajaxResult =
                sysDataSourceService.connDataBase(sysDataSourceConfig);
        DynamicDataSourceContextHolder.clearDataSourceType();
        return ajaxResult;
    }

    @ApiOperation("测试连接接口")
    @PostMapping("/test")
    public AjaxResult testConn(@RequestBody SysDataSourceConfig sysDataSourceConfig) {
        SysDataSourceService sysDataSourceService =
                loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.testConnection(sysDataSourceConfig, true);
    }

    @ApiOperation("获取连接信息接口")
    @GetMapping("/{configId}")
    public AjaxResult getSourceConfig(@PathVariable("configId") Long configId) {
        SysDataSourceConfig config = sysDataSourceService.getDataSourceConfig(configId);
        return AjaxResult.success(config);
    }

    @ApiOperation("获取数据库信息")
    @GetMapping("dataBase/{configId}")
    public AjaxResult getDataBase(@PathVariable("configId") Long configId,
                                  @RequestParam("dataBaseName") String dataBaseName) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.getDataBase(sysDataSourceConfig,dataBaseName);
    }

    @ApiOperation("获取某一数据库表接口")
    @PostMapping("/tables")
    public AjaxResult tables(@RequestBody SysDataSourceConfig config,
                             @RequestParam("databaseName") String databaseName) {
        return sysDataSourceService.getTables(config,databaseName);
    }


    @ApiOperation("删除连接接口")
    @DeleteMapping("/{configId}")
    public AjaxResult delete(@PathVariable("configId") Long configId) {
        return sysDataSourceService.delDataSource(configId);
    }

    @ApiOperation("关闭连接接口")
    @PostMapping("/close/{configId}")
    public AjaxResult closeConn(@PathVariable("configId") Long configId) {
        return sysDataSourceService.closeConn(configId);
    }

    @ApiOperation("添加数据库接口")
    @PostMapping("/addDB/{configId}")
    public AjaxResult addDataBase(@PathVariable("configId") Long configId,
                                  @RequestBody DataBaseConfigVo dataBaseConfigVo) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.addDataBase(sysDataSourceConfig, dataBaseConfigVo);
    }

    @ApiOperation("删除数据库接口")
    @DeleteMapping("/delDB/{configId}")
    public AjaxResult delDataBase(@PathVariable("configId") Long configId,
                                  @RequestParam("dataBaseName") String dataBaseName) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.delDataBase(sysDataSourceConfig,dataBaseName);
    }

    @ApiOperation("修改数据库信息")
    @PutMapping("/upDB/{configId}")
    public AjaxResult updateDataBase(@PathVariable("configId") Long configId,
                                     @RequestBody DataBaseConfigVo dataBaseConfig) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.updateDataBase(sysDataSourceConfig,dataBaseConfig);
    }


    @ApiOperation("添加表接口")
    @PostMapping("/addTB/{configId}")
    public AjaxResult addTable(@RequestBody Table table,
                               @PathVariable("configId") Long configId,
                               @RequestParam("dataBaseName") String dataBaseName) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.addTable(sysDataSourceConfig,dataBaseName,table);
    }

    @ApiOperation("删除表接口")
    @PostMapping("/delTB/{configId}")
    public AjaxResult delTable(@RequestParam("tableName") String tableName,
                               @PathVariable("configId") Long configId,
                               @RequestParam("dataBaseName") String dataBaseName) {
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.delTable(sysDataSourceConfig,dataBaseName,tableName);
    }

    @ApiOperation("获取表数据")
    @PostMapping("/tbData/{configId}")
    public AjaxResult getTBData(@RequestParam("tableName") String tableName,
                                @PathVariable("configId") Long configId,
                                @RequestParam("dataBaseName") String dataBaseName,
                                @RequestParam("current") int current,
                                @RequestParam("size") int size) {
        Page<Map<String, Object>> page = new Page<>(current, size);
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        List<Map<String, Object>> tableData = sysDataSourceService.getTableData(sysDataSourceConfig, dataBaseName, tableName, page);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(tableData);
        return AjaxResult.success(pageInfo);
    }

    @ApiOperation("删除表数据")
    @PostMapping("/delTbData/{configId}")
    public AjaxResult delTbData(@RequestParam("tableName") String tableName,
                                @PathVariable("configId") Long configId,
                                @RequestParam("dataBaseName") String dataBaseName,
                                @RequestBody Map<String,Object> map) {
        log.info("传输的map--{}",map);
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.delTbData(sysDataSourceConfig,tableName,dataBaseName,map);
    }

    @ApiOperation("编辑表数据")
    @PostMapping("/editTbData/{configId}")
    public AjaxResult editTbData(@RequestParam("tableName") String tableName,
                                @PathVariable("configId") Long configId,
                                @RequestParam("dataBaseName") String dataBaseName,
                                @RequestBody Map<String,Map<String,Object>> map) {
        log.info("传输的map--{}",map);
        SysDataSourceConfig sysDataSourceConfig = sysDataSourceService.getDataSourceConfig(configId);
        SysDataSourceService sysDataSourceService = loadDataSourceStrategy.getInstance(sysDataSourceConfig);
        return sysDataSourceService.updateTbData(sysDataSourceConfig,tableName,dataBaseName,map);
    }

}
