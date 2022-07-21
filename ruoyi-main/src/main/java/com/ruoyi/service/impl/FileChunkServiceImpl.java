package com.ruoyi.service.impl;

import com.ruoyi.domain.DO.FileChunkDO;
import com.ruoyi.mapper.FileChunkMapper;
import com.ruoyi.service.FileChunkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
public class FileChunkServiceImpl implements FileChunkService {

    @Autowired
    private FileChunkMapper fileChunkMapper;

    @Override
    public int saveFileChunkDO(FileChunkDO upFile) {
        return fileChunkMapper.insertFileChunkDO(upFile);
    }

    @Override
    public FileChunkDO getFileChunkDO(String bucketName, String uploadId) {
        return fileChunkMapper.getFileChunkDO(bucketName, uploadId);
    }

    @Override
    public int removeFileChunkDO(FileChunkDO upFile) {
        return fileChunkMapper.removeFileChunkDO(upFile);
    }
}
