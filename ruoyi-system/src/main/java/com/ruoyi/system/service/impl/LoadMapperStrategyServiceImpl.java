package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysDataBaseType;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.service.LoadMapperStrategyService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class LoadMapperStrategyServiceImpl implements LoadMapperStrategyService, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public Object loadMapper(SysDataSourceConfig sysDataSourceConfig, SysDataBaseType driver) {
        if (driver == null || driver.getDriver() != null) {
            return applicationContext.getBean("sysDataSourceConfigMapper");
        }
        if (driver.getDriver().contains("mysql")) {
            return applicationContext.getBean("sysDataSourceConfigMapper");
        } else if(driver.getDriver().contains("dm") ) {
            return applicationContext.getBean("sysDMDataSourceMapper");
        } else if (driver.getDriver().contains("oracle")) {
            return applicationContext.getBean("sysOracleDataSourceMapper");
        }
        return applicationContext.getBean("sysDataSourceConfigMapper");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
