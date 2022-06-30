package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.system.mapper.SysCharacterSetMapper;
import com.ruoyi.system.service.SysCharacterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysCharacterServiceImpl implements SysCharacterService {

    @Autowired
    private SysCharacterSetMapper sysCharacterSetMapper;

    @Override
    public AjaxResult listCharacterSet() {
        // 从主数据库中查询字符集
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        return AjaxResult.success(sysCharacterSetMapper.list());
    }
}
