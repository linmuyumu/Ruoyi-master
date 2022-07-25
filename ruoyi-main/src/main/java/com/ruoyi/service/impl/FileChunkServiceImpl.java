package com.ruoyi.service.impl;

import com.ruoyi.domain.DO.FileChunkDO;
import com.ruoyi.mapper.FileChunkMapper;
import com.ruoyi.service.FileChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
@Service
public class FileChunkServiceImpl implements FileChunkService {

    @Autowired
    private FileChunkMapper fileChunkMapper;

    /**
     * 保存分片上传信息
     * @param upFile
     * @return
     */
    @Override
    public int saveFileChunkDO(FileChunkDO upFile) {
        return fileChunkMapper.insertFileChunkDO(upFile);
    }

    /**
     * 根据 桶名+上传id 查询分片文件信息
     * @param bucketName
     * @param uploadId
     * @return
     */
    @Override
    public FileChunkDO getFileChunkDO(String bucketName, String uploadId) {
        return fileChunkMapper.getFileChunkDO(bucketName, uploadId);
    }

    /**
     * 物理删除分片上传信息
     * @param upFile
     * @return
     */
    @Override
    public int removeFileChunkDO(FileChunkDO upFile) {
        return fileChunkMapper.removeFileChunkDO(upFile);
    }
}
