package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.SysDataBaseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Mr.Yu
 * @create: 2022-06-25 21:23
 **/
@RestController
@RequestMapping("/system/database")
public class SysDataBaseTypeController {

    @Autowired
    SysDataBaseTypeService sysDataBaseTypeService;

    @GetMapping("/all")
    public AjaxResult getAll() {
        return AjaxResult.success(sysDataBaseTypeService.list());
    }

}
