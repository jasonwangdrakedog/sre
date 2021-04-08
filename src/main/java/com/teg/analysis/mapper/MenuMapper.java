package com.teg.analysis.mapper;

import com.teg.analysis.model.DO.MenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/7 13:09
 */
@Mapper
public interface MenuMapper {

    List<MenuDO> listMenu();
}
