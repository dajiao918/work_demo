package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CharacterSet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysCharacterSetMapper {

    public List<CharacterSet> list();

}
