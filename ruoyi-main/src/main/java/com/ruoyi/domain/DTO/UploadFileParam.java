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
public class UploadFileParam {

    private String filePath;

    private String uploadTime;

    private String fileName;

    private Long fileSize;

    private int chunkNumber;

    private String identifier;

    private String fileType;
}
