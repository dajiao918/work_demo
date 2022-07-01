package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.vo.DataBaseConfigVo;
import com.ruoyi.system.domain.vo.OracleScheMetaVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysOracleMapper {

    List<OracleScheMetaVo> getDataBases();

    void createDataBase(@Param("config") DataBaseConfigVo dataBaseConfigVo);
}
