package com.ruoyi.domain.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @since 2022-04-22
 */
@Data
@ToString
public class FileChunkDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fileName;

    private String fileUrl;

    private Long fileSize;

    private String fileType;

    private String fileMD5;

    private Integer chunkNumber;

    private Date uploadTime;

    private String bucketName;

    private String idWorkerName;

    private String uploadId;


}
