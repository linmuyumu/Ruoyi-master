package com.ruoyi.service;

import com.ruoyi.domain.DO.FileChunkDO;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
public interface FileChunkService {

    /**
     * 保存分片上传信息
     * @param upFile
     * @return
     */
    int saveFileChunkDO(FileChunkDO upFile);

    /**
     * 根据 桶名+上传id 查询分片文件信息
     * @param bucketName
     * @param uploadId
     * @return
     */
    FileChunkDO getFileChunkDO(String bucketName, String uploadId);

    /**
     * 物理删除分片上传信息
     * @param upFile
     * @return
     */
    int removeFileChunkDO(FileChunkDO upFile);
}
