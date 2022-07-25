package com.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.CreateFileParam;
import com.ruoyi.domain.DTO.SelectFileParam;
import com.ruoyi.mapper.FileMapper;
import com.ruoyi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<FileDO> fileList(String bucketName, FileDO fileDO) {
        return null;
    }

    @Override
    public FileDO selectDirByPath(String bucketName, SelectFileParam selectFileParam) {
        return null;
    }

    @Override
    public int saveFileDO(FileDO userFile) {
        return 0;
    }

    @Override
    public List<FileDO> getListByName(String bucketName, String filename, Long pageNum, Long pageSize) {
        return null;
    }

    @Override
    public List<FileDO> getListByType(String bucketName, String fileType, Long pageNum, Long pageSize) {
        return null;
    }

    @Override
    public List<FileDO> getfileRecycle(String bucketName, Long pageNum, Long pageSize) {
        return null;
    }

    @Override
    public int count(LambdaQueryWrapper<FileDO> fileRecycle) {
        return 0;
    }

    @Override
    public int updateFileDOById(FileDO fileDO) {
        return 0;
    }

    @Override
    public int updateByFileId(FileDO file) {
        return 0;
    }

    @Override
    public FileDO selectFileDOByParams(FileDO fileDO) {
        return null;
    }

    @Override
    public FileDO getById(int id) {
        return null;
    }

    @Override
    public int removeById(int id) {
        return 0;
    }

    @Override
    public int remove(LambdaQueryWrapper<FileDO> lambdaQueryWrapper) {
        return 0;
    }

    @Override
    public int restoreTrash(String bucketName) {
        return 0;
    }

    @Override
    public int setDelTimeNull(int id) {
        return 0;
    }

    @Override
    public List<FileDO> selectFilePathTreeByBucket(String bucketName) {
        return null;
    }

    @Override
    public int updateFilePath(String bucketName, String fileOldPath, String fileNewPath) {
        return 0;
    }
}
