package com.teg.analysis.mapper;

import com.teg.analysis.model.DO.SecondLineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/7 13:09
 */
@Mapper
public interface SecondLineMapper {

    List<SecondLineDO> listMenu();
}
