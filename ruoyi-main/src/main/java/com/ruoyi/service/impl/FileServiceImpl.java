package com.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.SelectFileParam;
import com.ruoyi.mapper.FileMapper;
import com.ruoyi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Autowired
    private FileMapper fileMapper;

    /**
     * 查询文件列表
     * @param fileDO
     * @return
     */
    @Override
    public List<FileDO> fileList(String bucketName, FileDO fileDO) {
        fileDO.setBucketName(bucketName);
        return fileMapper.fileList(fileDO);
    }

    /**
     * 查询文件夹信息
     * @param bucketName
     * @param selectFileParam
     * @return
     */
    @Override
    public FileDO selectDirByPath(String bucketName, SelectFileParam selectFileParam) {
        return fileMapper.selectDirByPath(bucketName, selectFileParam.getFilePath(), selectFileParam.getFileName());
    }

    /**
     * 保存文件
     * @param fileDO
     * @return
     */
    @Override
    public int saveFileDO(FileDO fileDO) {
        return fileMapper.saveFileDO(fileDO);
    }

    /**
     * 按名模糊查询文件列表
     * @param bucketName
     * @param filename
     * @param pageNum
     * @param pageSize
     * @return
     */
    /*@Override
    public List<FileDO> getListByName(String bucketName, String filename, Long pageNum, Long pageSize) {
        return null;
    }*/

    /**
     * 修改文件信息
     * @param fileDO
     * @return
     */
    @Override
    public int updateFileById(FileDO fileDO) {
        return fileMapper.updateFileById(fileDO);
    }

    /**
     * 批量删除文件
     * @param files
     * @return
     */
    @Override
    public int updateFileByIds(List<FileDO> files) {
        return fileMapper.updateFileByIds(files);
    }

    /**
     * 查询重名文件
     * @param fileDO
     * @return
     */
    @Override
    public FileDO selectFileByFileDO(FileDO fileDO) {
        return fileMapper.selectFileByFileDO(fileDO);
    }

    /**
     * 删除回收站里的某个文件
     * @param id
     * @return
     */
    @Override
    public int removeFileById(int id) {
        return fileMapper.removeFileById(id);
    }

    /**
     * 清空回收站
     * @return
     */
    @Override
    public int removeAllFile() {
        return fileMapper.removeAllFile();
    }

    /**
     * 还原回收站单个文件
     * @param bucketName
     * @return
     */
    @Override
    public int restoreTrash(String bucketName, int id) {
        return fileMapper.restoreTrash(bucketName, id);
    }

    /**
     * 还原回收站多个文件
     * @param files
     * @return
     */
    @Override
    public int updateFile(List<FileDO> files) {
        return fileMapper.updateFile(files);
    }

    /**
     * 根据id查询文件
     * @param id
     * @return
     */
    @Override
    public FileDO getFileById(Integer id) {
        return fileMapper.selectById(id);
    }

    /**
     * 获取文件树
     * @param bucketName
     * @return
     */
    @Override
    public List<FileDO> selectFilePathTreeByBucket(String bucketName) {
        LambdaQueryWrapper<FileDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FileDO::getBucketName, bucketName)
                .isNull(FileDO::getFileSize).isNull(FileDO::getFileDelDate);
        return fileMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 文件移动
     * @param bucketName
     * @param fileDO
     * @return
     */
    @Override
    public int updateFilePath(String bucketName, FileDO fileDO) {
        fileDO.setBucketName(bucketName);
        return fileMapper.updateFilePath(fileDO);
    }
}
