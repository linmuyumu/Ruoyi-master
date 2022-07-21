package com.ruoyi.service;

import com.ruoyi.domain.DO.FileChunkDO;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
public interface FileChunkService {

    int saveFileChunkDO(FileChunkDO upFile);

    FileChunkDO getFileChunkDO(String bucketName, String uploadId);

    int removeFileChunkDO(FileChunkDO upFile);
}
