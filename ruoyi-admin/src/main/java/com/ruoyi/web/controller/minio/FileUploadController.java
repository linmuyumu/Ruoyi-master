package com.ruoyi.web.controller.minio;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.HashMultimap;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.domain.DO.FileChunkDO;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.UploadFileParam;
import com.ruoyi.service.FileChunkService;
import com.ruoyi.service.FileService;
import com.ruoyi.utils.MinioUtils;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月16日
 */
@RestController
@RequestMapping("/file/upload")
public class FileUploadController {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileChunkService fileChunkService;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 返回分片上传需要的签名数据URL及 uploadId
     *
     * @param upFileParam
     * @return
     */
    @GetMapping("/createMultipartUpload")
    @SneakyThrows
    @ResponseBody
    public Map<String, Object> createMultipartUpload(UploadFileParam upFileParam) {
        int chunkSize = upFileParam.getChunkNumber();
        FileChunkDO upFile = new FileChunkDO();
        String TypeName = "";
        if (upFileParam.getFileName().contains(".")) {
            upFile.setFileName(upFileParam.getFileName().substring(0, upFileParam.getFileName().indexOf(".")));
            TypeName = upFileParam.getFileName().substring(upFileParam.getFileName().lastIndexOf(".")).substring(1);
        } else {
            upFile.setFileName(upFileParam.getFileName());
        }
        upFile.setFileUrl(upFileParam.getFilePath());
        upFile.setBucketName(bucketName);
        upFile.setFileSize(upFileParam.getFileSize());
        upFile.setFileMD5(upFileParam.getIdentifier());
        upFile.setUploadTime(DateUtils.getNowDate());
        upFile.setFileType(upFileParam.getFileType());
        upFile.setChunkNumber(chunkSize);

        //IdWorker 重命名
        String fileName = IdWorker.getId() + TypeName;
        upFile.setIdWorkerName(fileName);

        // 根据文件名创建签名
        Map<String, Object> result = new HashMap<>();
        // 获取uploadId
        String contentType = "application/octet-stream";
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        CreateMultipartUploadResponse response = minioUtils.uploadId(bucketName, null, fileName, headers, null);
        String uploadId = response.result().uploadId();
        result.put("uploadId", uploadId);

        upFile.setUploadId(uploadId);
        System.out.println(upFile);
        fileChunkService.saveFileChunkDO(upFile);

        // 请求Minio 服务，获取每个分块带签名的上传URL
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        // 循环分块数 从1开始,MinIO 存储服务定义分片索引却是从1开始的
        for (int i = 1; i <= chunkSize; i++) {
            reqParams.put("partNumber", String.valueOf(i));
            // 获取URL,主要这里前端上传的时候，要传递二进制流，而不是file
            String uploadUrl = minioUtils.getPresignedObjectUrl(bucketName, fileName, reqParams);
            // 添加到集合
            result.put("chunk_" + (i - 1), uploadUrl);
        }
        return result;
    }


    /**
     * 分片上传完后合并
     *
     * @param fileName 文件名
     * @param uploadId 返回的uploadId
     * @return /
     */
    @GetMapping("/completeMultipartUpload")
    @SneakyThrows
    @ResponseBody
    public boolean completeMultipartUpload(String fileName, String uploadId) {
        FileChunkDO upFile = fileChunkService.getFileChunkDO(bucketName, uploadId);
        String objectName = upFile.getIdWorkerName();
        try {
            Part[] parts = new Part[10000];
            // 1. 查询分片
            ListPartsResponse partResult = minioUtils.listMultipart(bucketName, null, objectName, 10000, 0, uploadId, null, null);
            // 分片序列从1开始
            int partNumber = 1;
//            System.err.println(partResult.result().partList().size() + "========================");
            // 2. 循环获取到的分片信息
            List<Part> partList = partResult.result().partList();
            for (int i = 0; i < partList.size(); i++) {
                // 3. 将分片标记传递给Part对象
                parts[partNumber - 1] = new Part(partNumber, partList.get(i).etag());
                partNumber++;
            }
            minioUtils.completeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
        } catch (Exception e) {
            return false;
        }
        FileDO fileDO = new FileDO();
        fileDO.setBucketName(bucketName);
        fileDO.setFileInbuckName(objectName);
        fileDO.setFileType(upFile.getFileType());
        fileDO.setIsDirectory("0");
        fileDO.setFilePath(upFile.getFileUrl());
        fileDO.setFileSize(upFile.getFileSize());
        fileDO.setFileName(upFile.getFileName());
        fileDO.setFileMD5(upFile.getFileMD5());
        fileDO.setFileDate(DateUtils.getNowDate());
        fileDO.setCreateBy(SecurityUtils.getUsername());
        fileDO.setCreateTime(DateUtils.getNowDate());
        fileDO.setUserId(SecurityUtils.getUserId());
        fileService.saveFileDO(fileDO);
        fileChunkService.removeFileChunkDO(upFile);
        return true;
    }


    /**
     * 下载文件
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param response   相应结果
     */
    @GetMapping("/downFile")
    public void downLoad(@RequestParam(required = false) String bucketName, String objectName, HttpServletResponse response) {
        bucketName = StringUtils.hasLength(bucketName) ? bucketName : this.bucketName;
        // 获取文件
        minioUtils.downResponse(bucketName, objectName, response);
    }
}
