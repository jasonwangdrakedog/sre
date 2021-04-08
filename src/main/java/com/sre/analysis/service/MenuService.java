package com.sre.analysis.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sre.analysis.model.DO.MenuDO;
import com.sre.analysis.mapper.MenuMapper;
import com.sre.analysis.model.DTO.MenuDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;


    public List<MenuDTO> listMenu() {
        List<MenuDTO> dtos = Lists.newArrayList();
        List<MenuDO> dos = menuMapper.listMenu();
        Map<Integer, MenuDTO> map = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(dos)) {
            List<MenuDO> firstMenu = dos.stream().filter(a -> a.getLevel() == 1).collect(Collectors.toList());
            firstMenu.forEach(menuDO -> {
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setId(menuDO.getId());
                menuDTO.setMenuName(menuDO.getMenuName());
                menuDTO.setChildren(Lists.newArrayList());
                menuDTO.setPath(menuDO.getPath());
                dtos.add(menuDTO);
                map.put(menuDO.getId(), menuDTO);
            });

            dos.forEach(menuDO -> {
                if (menuDO.getLevel() > 1) {
                    MenuDTO menuDTO = new MenuDTO();
                    menuDTO.setId(menuDO.getId());
                    menuDTO.setMenuName(menuDO.getMenuName());
                    menuDTO.setPath(menuDO.getPath());
                    map.get(menuDO.getParentId()).getChildren().add(menuDTO);
                }
            });
        }
        return dtos;
    }
}
