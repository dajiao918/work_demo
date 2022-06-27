package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.DynamicDataSourceContextHolder;
import com.ruoyi.system.domain.vo.DataSourceConfig;
import com.ruoyi.system.service.SysMySQLDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 23:16
 **/
@RestController
@RequestMapping("/system/dataSource")
public class SystemDataSourceController {

    @Autowired
    SysMySQLDataSourceService sysMySQLDataSourceService;

    @GetMapping("/history")
    public AjaxResult historyDatabases() {
        return sysMySQLDataSourceService.history(SecurityUtils.getUserId());
    }

    @PostMapping("/databases")
    public AjaxResult databases(@RequestBody DataSourceConfig dataSourceConfig) {
        dataSourceConfig.setUserId(SecurityUtils.getUserId());
        AjaxResult ajaxResult =
                sysMySQLDataSourceService.connDataBase(dataSourceConfig);
        DynamicDataSourceContextHolder.clearDataSourceType();
        return ajaxResult;
    }

    @PostMapping("/test")
    public AjaxResult test(@RequestBody DataSourceConfig dataSourceConfig) {
        return sysMySQLDataSourceService.testConnection(dataSourceConfig, true);
    }

    @PostMapping("/tables")
    public AjaxResult tables(@RequestBody DataSourceConfig config,
                             @RequestParam("databaseName") String databaseName) {
        return sysMySQLDataSourceService.getTables(config,databaseName);
    }

    @DeleteMapping("/{configId}")
    public AjaxResult delete(@PathVariable("configId") Long configId) {
       return sysMySQLDataSourceService.delDataSource(configId);
    }


}
