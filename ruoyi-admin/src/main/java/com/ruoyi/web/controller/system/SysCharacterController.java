package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysDataSourceConfig;
import com.ruoyi.system.service.SysCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/character")
public class SysCharacterController {

    @Autowired
    private SysCharacterService  sysCharacterService;

    @GetMapping("list")
    public AjaxResult list() {
        return sysCharacterService.listCharacterSet();
    }

}
