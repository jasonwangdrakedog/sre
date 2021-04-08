package com.teg.analysis.model.DO;

import com.teg.analysis.model.REQ.UserREQ;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author wangyuan
 * @date 2020/8/7 13:13
 */
@Data
public class UserDO {



    private Long userId;

    private String birthday;

    private String userName;

    private String userAddress;

    private Boolean sex;

    private Integer height;

    private Integer weight;

    private String province;

    private Date createDate;

    public UserDO(UserREQ req) {
        BeanUtils.copyProperties(req, this);
    }

    public UserDO() {
    }
}
