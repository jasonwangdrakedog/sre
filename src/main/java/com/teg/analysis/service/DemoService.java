package com.teg.analysis.service;

import com.google.common.collect.Lists;
import com.teg.analysis.mapper.UserMapper;
import com.teg.analysis.model.DO.UserDO;
import com.teg.analysis.model.DTO.UserDTO;
import com.teg.analysis.model.REQ.UserREQ;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

    @Resource
    private UserMapper UserMapper;

    public void createUser(UserREQ userREQ) {
        UserMapper.createUser(new UserDO(userREQ));
    }

    public Integer updateUser(UserREQ userREQ) {
        return UserMapper.updateUser(new UserDO(userREQ));
    }

    public void batchCreateUser(List<UserREQ> list) {
        if (!CollectionUtils.isEmpty(list)) {
            List<UserDO> list1 = Lists.newArrayList();
            list.forEach(userREQ -> list1.add(new UserDO(userREQ)));
            UserMapper.batchCreateUser(list1);
        }
    }

    public List<UserDTO> listUser() {
        List<UserDTO> dtos = Lists.newArrayList();
        List<UserDO> dos = UserMapper.queryAllUsers();
        if (!CollectionUtils.isEmpty(dos)) {
            dos.forEach(userDO -> dtos.add(new UserDTO(userDO)));
        }
        return dtos;
    }
}
