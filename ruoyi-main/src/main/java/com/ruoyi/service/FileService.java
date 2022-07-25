package com.ruoyi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.SelectFileParam;

import java.util.List;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
public interface FileService extends IService<FileDO> {

    /**
     * 查询文件列表
     * @param bucketName
     * @param fileDO
     * @return
     */
    List<FileDO> fileList(String bucketName, FileDO fileDO);

    /**
     * 查询文件夹信息
     * @param bucketName
     * @param selectFileParam
     * @return
     */
    FileDO selectDirByPath(String bucketName, SelectFileParam selectFileParam);

    /**
     * 保存文件
     * @param fileDO
     * @return
     */
    int saveFileDO(FileDO fileDO);

    /**
     * 按名模糊查询文件列表
     * @param bucketName
     * @param filename
     * @param pageNum
     * @param pageSize
     * @return
     */
//    List<FileDO> getListByName(String bucketName, String filename, Long pageNum, Long pageSize);

    /**
     * 修改文件信息
     * @param fileDO
     * @return
     */
    int updateFileById(FileDO fileDO);

    /**
     * 批量删除文件
     * @param files
     * @return
     */
    int updateFileByIds(List<FileDO> files);

    /**
     * 查询重名文件
     * @param fileDO
     * @return
     */
    FileDO selectFileByFileDO(FileDO fileDO);

    /**
     * 删除回收站里的某个文件
     * @param id
     * @return
     */
    int removeFileById(int id);

    /**
     * 清空回收站
     * @return
     */
    int removeAllFile();

    /**
     * 还原回收站单个文件
     * @param bucketName
     * @return
     */
    int restoreTrash(String bucketName, int id);

    /**
     * 还原多个文件
     * @param files
     * @return
     */
    int updateFile(List<FileDO> files);

    /**
     * 根据id查询文件
     * @param id
     * @return
     */
    FileDO getFileById(Integer id);

    /**
     * 获取文件树
     * @param bucketName
     * @return
     */
    List<FileDO> selectFilePathTreeByBucket(String bucketName);

    /**
     * 文件移动
     * @param bucketName
     * @param fileDO
     * @return
     */
    int updateFilePath(String bucketName, FileDO fileDO);

}
