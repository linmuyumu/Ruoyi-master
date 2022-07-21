package com.ruoyi.domain.VO;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
@Data
@ToString
public class TreeNodeVO {

    private Long id;

    private String label;

    private Long depth;

    private String state = "closed";

    private Map<String, String> attributes;

    private List<TreeNodeVO> children;
}
