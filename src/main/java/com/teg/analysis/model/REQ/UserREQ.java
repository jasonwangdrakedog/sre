package com.teg.analysis.model.REQ;

import lombok.Data;


/**
 * 用户注册
 */
@Data
public class UserREQ {

    private  Long userId;

    private String birthday;

    private String userName;

    private String userAddress;

    private Integer sex;

    private Integer height;

    private Integer weight;

    private String province;
}
