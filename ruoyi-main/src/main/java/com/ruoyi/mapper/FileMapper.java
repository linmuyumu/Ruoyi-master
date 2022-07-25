package com.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.domain.DO.FileDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年07月16日
 */
@Repository
public interface FileMapper extends BaseMapper<FileDO> {

    /**
     * 查询文件列表
     * @param fileDO
     * @return
     */
    List<FileDO> fileList(FileDO fileDO);

    /**
     * 查询文件夹信息
     * @param bucketName
     * @param filePath
     * @param fileName
     * @return
     */
    FileDO selectDirByPath(@Param("bucketName") String bucketName, @Param("filePath") String filePath, @Param("fileName") String fileName);

    /**
     * 保存文件
     * @param fileDO
     * @return
     */
    int saveFileDO(FileDO fileDO);

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
     * @param id
     * @return
     */
    int restoreTrash(@Param("bucketName") String bucketName, @Param("id") int id);

    /**
     * 还原回收站多个文件
     * @param files
     * @return
     */
    int updateFile(List<FileDO> files);

    int updateFilePath(FileDO fileDO);
}
