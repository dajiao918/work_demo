package com.ruoyi.web.controller.strategy;

import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.service.SysDataBaseTypeService;
import com.ruoyi.system.service.SysDataSourceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: Mr.Yu
 * @create: 2022-06-27 11:23
 **/
@Component
public class LoadDataSourceStrategyImpl implements LoadDataSourceStrategy, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SysDataBaseTypeService sysDataBaseTypeService;
    @Qualifier("mysqlDataSourceServiceImpl")
    @Autowired
    private SysDataSourceService sysDataSourceService;

    @Override
    public SysDataSourceService getInstance(SysDataSourceConfig config) {
        if (config.getType() == null) {
            return (SysDataSourceService) applicationContext.getBean("mysqlDataSourceServiceImpl");
        }
        SysDataBaseType driver = sysDataBaseTypeService.getDriver(config.getType());
        String driverName = driver.getDriver();
        if (driverName != null) {
            if (driverName.contains("mysql")) {
                return (SysDataSourceService) applicationContext.getBean("mysqlDataSourceServiceImpl");
            } else if (driverName.contains("dm")) {
                return (SysDataSourceService) applicationContext.getBean("dmDataSourceServiceImpl");
            } else if (driverName.contains("oracle")) {
                return (SysDataSourceService) applicationContext.getBean("oracleDataSourceServiceImpl");
            }
        }
        return (SysDataSourceService) applicationContext.getBean("mysqlDataSourceServiceImpl");
    }

    @Override
    public SysDataSourceService getInstance(Long configId) {
        SysDataSourceConfig config = sysDataSourceService.getDataSourceConfig(configId);
        return getInstance(config);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
