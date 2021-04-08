package com.teg.analysis.model.DTO;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/13 9:27
 */
@Data
public class MenuDTO {
    private Integer id;

    private String menuName;

    private List<MenuDTO> children;

    private String path;
}
