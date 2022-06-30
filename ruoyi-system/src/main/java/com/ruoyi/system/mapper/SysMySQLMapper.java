package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Table;
import com.ruoyi.system.domain.vo.MySQLTableMetaVo;
import com.ruoyi.system.domain.vo.MysqlSheMetaVo;
import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysMySQLMapper {

    public List<MysqlSheMetaVo> metaList();

    List<MySQLTableMetaVo> getTables(String databaseName);

    MySQLTableMetaVo getTable(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

    void createDatabase(@Param("config") DataBaseConfigVo config);

    void delDataBase(String dataBaseName);

    void updateDataBase(@Param("config") DataBaseConfigVo config);

    void createTable(@Param("table") Table table);

    void delTable(@Param("tableName")String tableName);

    List<Map<String,Object>> getTableData(@Param("tableName")String tableName);
}
