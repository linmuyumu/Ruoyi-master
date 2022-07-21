package com.ruoyi.domain.DTO;

import lombok.Data;
import lombok.ToString;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
@Data
@ToString
public class FileUpdateParam {

    private int id;

    private String fileName;

    private String fileUrl;

    private String delFlag;

    private String isDirectory;
}
