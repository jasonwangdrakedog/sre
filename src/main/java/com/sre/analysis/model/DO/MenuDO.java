package com.sre.analysis.model.DO;

import lombok.Data;

/**
 * @author wangyuan
 * @date 2020/8/13 14:17
 */
@Data
public class MenuDO {

    private Integer id;

    private String menuName;

    private String path;

    private Integer level;

    private Integer parentId;
}
