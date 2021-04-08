package com.sre.analysis.model.DTO;

import com.sre.analysis.model.DO.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;


/**
 * 用户注册
 */
@Data
public class UserDTO {

    private Long userId;

    private String birthday;

    private String userName;

    private String userAddress;

    private Integer sex;

    private Integer height;

    private Integer weight;

    private String province;

    private Long createDate;

    public UserDTO(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
        if (!ObjectUtils.isEmpty(userDO.getCreateDate())) {
            this.createDate = userDO.getCreateDate().getTime();
        }
    }
}
