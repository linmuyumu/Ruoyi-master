package com.ruoyi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.CreateFileParam;
import com.ruoyi.domain.DTO.SelectFileParam;

import java.util.List;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
public interface FileService {

    List<FileDO> fileList(String bucketName, FileDO fileDO);

    FileDO selectDirByPath(String bucketName, SelectFileParam selectFileParam);

    int saveFileDO(FileDO userFile);

    List<FileDO> getListByName(String bucketName, String filename, Long pageNum, Long pageSize);

    List<FileDO> getListByType(String bucketName, String fileType, Long pageNum, Long pageSize);

    List<FileDO> getfileRecycle(String bucketName, Long pageNum, Long pageSize);

    int count(LambdaQueryWrapper<FileDO> fileRecycle);

    int updateFileDOById(FileDO fileDO);

    int updateByFileId(FileDO file);

    FileDO selectFileDOByParams(FileDO fileDO);

    FileDO getById(int id);

    int removeById(int id);

    int remove(LambdaQueryWrapper<FileDO> lambdaQueryWrapper);

    int restoreTrash(String bucketName);

    int setDelTimeNull(int id);

    List<FileDO> selectFilePathTreeByBucket(String bucketName);

    int updateFilePath(String bucketName, String fileOldPath, String fileNewPath);
}
