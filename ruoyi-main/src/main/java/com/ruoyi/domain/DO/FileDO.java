package com.ruoyi.domain.DO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
@Data
@ToString
public class FileDO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    private Long patientId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件类型
     */
    private String fileClass;

    /**
     * 文件存储IDwork名
     */
    private String fileInbuckName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * MD5唯一标识
     */
    private String fileMD5;

    /**
     * 检查时间
     */
    private String checkTime;

    /**
     * 检查日期
     */
    private String checkDate;

    /**
     * 存储桶
     */
    private String bucketName;

    /**
     * 上传日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fileDate;

    /**
     * 删除日期
     */
    private String fileDelDate;

    /**
     * 状态 0不是文件夹，1是文件夹
     */
    private String isDirectory;

    /**
     * 状态 0未删除，1已删除
     */
    private String delFlag;

    private Long userId;

    /**
     * 旧路径
     */
    private String fileOldPath;

    /**
     * 新路径
     */
    private String fileNewPath;
}
