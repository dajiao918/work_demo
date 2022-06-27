package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.DynamicDataSourceContextHolder;
import com.ruoyi.system.domain.vo.DataSourceConfig;
import com.ruoyi.system.service.SysDataSourceService;
import com.ruoyi.web.controller.strategy.LoadDataSourceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 23:16
 **/
@RestController
@RequestMapping("/system/dataSource")
public class SystemDataSourceController {

    @Qualifier("AbstractDataSourceServiceImpl")
    @Autowired
    SysDataSourceService sysDataSourceService;
    @Autowired
    LoadDataSourceStrategy loadDataSourceStrategy;

    @GetMapping("/history")
    public AjaxResult historyDatabases() {
        return sysDataSourceService.history(SecurityUtils.getUserId());
    }

    @PostMapping("/databases")
    public AjaxResult connDatabases(@RequestBody DataSourceConfig dataSourceConfig) {
        dataSourceConfig.setUserId(SecurityUtils.getUserId());
        SysDataSourceService sysDataSourceService =
                loadDataSourceStrategy.getInstance(dataSourceConfig);
        AjaxResult ajaxResult =
                sysDataSourceService.connDataBase(dataSourceConfig);
        DynamicDataSourceContextHolder.clearDataSourceType();
        return ajaxResult;
    }

    @PostMapping("/test")
    public AjaxResult testConn(@RequestBody DataSourceConfig dataSourceConfig) {
        SysDataSourceService sysDataSourceService =
                loadDataSourceStrategy.getInstance(dataSourceConfig);
        return sysDataSourceService.testConnection(dataSourceConfig, true);
    }

    @PostMapping("/tables")
    public AjaxResult tables(@RequestBody DataSourceConfig config,
                             @RequestParam("databaseName") String databaseName) {
        return sysDataSourceService.getTables(config,databaseName);
    }

    @DeleteMapping("/{configId}")
    public AjaxResult delete(@PathVariable("configId") Long configId) {
        return sysDataSourceService.delDataSource(configId);
    }


}
