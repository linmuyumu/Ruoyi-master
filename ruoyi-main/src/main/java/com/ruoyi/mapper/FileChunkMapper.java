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

    int insertFileChunkDO(FileChunkDO upFile);

    FileChunkDO getFileChunkDO(@Param("bucketName") String bucketName, @Param("uploadId") String uploadId);

    int removeFileChunkDO(FileChunkDO upFile);
}
