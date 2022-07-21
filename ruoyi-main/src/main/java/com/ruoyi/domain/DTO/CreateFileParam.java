package com.ruoyi.domain.DTO;

import lombok.Data;
import lombok.ToString;

/**
 * @author diaozhiqiang
 * @description 描述： 新建文件接口
 * @date 2022年07月16日
 */

@Data
@ToString
public class CreateFileParam {

    private String fileName;

    private String filePath;

    private Long currentPage;

    private Long pageCount;
}
