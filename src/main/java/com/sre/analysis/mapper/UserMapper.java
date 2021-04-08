package com.sre.analysis.mapper;

import com.sre.analysis.model.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/7 13:09
 */
@Mapper
public interface UserMapper {

    List<UserDO> queryAllUsers();


    void createUser(UserDO userDO);


    Integer updateUser(UserDO userDO);


    void batchCreateUser(@Param("list") List<UserDO> list);

}
