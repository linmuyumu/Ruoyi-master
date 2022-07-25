package com.ruoyi.domain.DTO;

import lombok.Data;
import lombok.ToString;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
@Data
@ToString
public class SelectFileParam {

    private String files;

    private String fileName;

    private String filePath;

    private String fileType;

    private Long pageSize;

    private Long pageNum;
}
