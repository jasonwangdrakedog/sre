package com.sre.analysis.controller;


import com.sre.analysis.model.common.Page;
import com.sre.analysis.service.MenuService;
import com.sre.analysis.util.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangyuan
 * @date 2020/8/13 9:07
 */
@RestController
public class MenuController {

    @Resource
    private MenuService menuService;

    @RequestMapping(value = "menus", method = RequestMethod.GET)
    @ResponseBody
    public Page menuList() {
        return PageUtil.success(menuService.listMenu());
    }
}
