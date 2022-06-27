package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.MySQLTable;
import com.ruoyi.system.domain.MysqlSheMeta;
import com.ruoyi.system.domain.vo.DataSourceConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Mr.Yu
 * @create: 2022-06-24 23:03
 **/
@Repository
public interface SysDataSourceMapper {

    public List<MysqlSheMeta> metaList();

    public List<MySQLTable> getTables(String databaseName);

    DataSourceConfig getDataSourceConfig(Long configId);

    void delete(Long configId);

    void save(@Param("config") DataSourceConfig config);

    List<DataSourceConfig> histories(Long userId);
}
