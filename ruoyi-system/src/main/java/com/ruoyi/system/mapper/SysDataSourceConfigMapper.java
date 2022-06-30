package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysDataSourceConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 23:03
 **/
@Repository
public interface SysDataSourceConfigMapper {



    SysDataSourceConfig getDataSourceConfig(Long configId);

    void delete(Long configId);

    void save(@Param("config") SysDataSourceConfig config);

    List<SysDataSourceConfig> histories(Long userId);

    void update(@Param("config") SysDataSourceConfig config);
}
