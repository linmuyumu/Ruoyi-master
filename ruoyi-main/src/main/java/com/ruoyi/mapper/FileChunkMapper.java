package com.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.domain.DO.FileChunkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author admin
 */
@Repository
public interface FileChunkMapper extends BaseMapper<FileChunkDO> {

    /**
     * 保存分片上传信息
     * @param upFile
     * @return
     */
    int insertFileChunkDO(FileChunkDO upFile);

    /**
     * 根据 桶名+上传id 查询分片文件信息
     * @param bucketName
     * @param uploadId
     * @return
     */
    FileChunkDO getFileChunkDO(@Param("bucketName") String bucketName, @Param("uploadId") String uploadId);

    /**
     * 物理删除分片上传信息
     * @param upFile
     * @return
     */
    int removeFileChunkDO(FileChunkDO upFile);
}
