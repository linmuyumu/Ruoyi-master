package com.ruoyi.web.controller.minio;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.domain.DO.FileDO;
import com.ruoyi.domain.DTO.FileUpdateParam;
import com.ruoyi.domain.DTO.SelectFileParam;
import com.ruoyi.domain.VO.TreeNodeVO;
import com.ruoyi.service.FileService;
import com.ruoyi.utils.MinioUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author diaozhiqiang
 * @description 描述： minio上传控制器
 * @date 2022年06月07日
 */
@RestController
@RequestMapping("/minio")
public class MinioController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${ruoyi.downLoad}")
    private String downLoadPath;

    @GetMapping("/list")
    public TableDataInfo fileList(FileDO fileDO) {
        startPage();
        List<FileDO> list = fileService.fileList(bucketName, fileDO);
        return getDataTable(list);
    }

    @PostMapping(value = "/mkDir")
    public AjaxResult createFile(@RequestBody SelectFileParam selectFileParam) {
        FileDO fileDO = fileService.selectDirByPath(bucketName, selectFileParam);
        if (StringUtils.isNotNull(fileDO)) {
            return AjaxResult.error("同名路径已存在");
        }
        FileDO userFile = new FileDO();
        userFile.setBucketName(bucketName);
        userFile.setFileName(selectFileParam.getFileName());
        userFile.setFilePath(selectFileParam.getFilePath());
        userFile.setIsDirectory("1");
        userFile.setCreateBy(SecurityUtils.getUsername());
        userFile.setCreateTime(DateUtils.getNowDate());
        userFile.setUserId(SecurityUtils.getUserId());
        minioUtils.newPath(bucketName, selectFileParam.getFilePath());
        fileService.saveFileDO(userFile);
        return AjaxResult.success("新建路径成功");
    }

    @ApiOperation(value = "按名模糊查询")
    @GetMapping("/list/search")
    public AjaxResult getListByName(SelectFileParam selectFileParam) {
        String filename = '%' + selectFileParam.getFileName() + '%';
        List<FileDO> fileListByName = fileService.getListByName(bucketName, filename, selectFileParam.getPageNum(), selectFileParam.getPageSize());
        return AjaxResult.success("", fileListByName);
    }

    @ApiOperation(value = "按类搜索文件")
    @GetMapping("/list/type")
    public AjaxResult listType(SelectFileParam selectFileParam) {
        if (Integer.parseInt(selectFileParam.getFileType()) < 7) {
            List<FileDO> fileTypeList = fileService.getListByType(bucketName, selectFileParam.getFileType(), selectFileParam.getPageNum(), selectFileParam.getPageSize());
            int total = 0;
            System.out.print(total);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("list", fileTypeList);
            return AjaxResult.success("查询成功", map);
        }
        if (Integer.parseInt(selectFileParam.getFileType()) == 8) {
            List<FileDO> fileListRecycle = fileService.getfileRecycle(bucketName, selectFileParam.getPageNum(), selectFileParam.getPageSize());
            LambdaQueryWrapper<FileDO> FileRecycle = new LambdaQueryWrapper<>();
            FileRecycle.eq(FileDO::getBucketName, bucketName)
                    .isNotNull(FileDO::getFileDelDate);
            int total = fileService.count(FileRecycle);
            System.out.print(total);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("list", fileListRecycle);
            return AjaxResult.success("", map);
        }
        if (Integer.parseInt(selectFileParam.getFileType()) == 9) {
//            List<map> sharefils = fileService.getsharefiles(bucketName,fileListByTypeParam.getCurrentPage(),fileListByTypeParam.getPageCount());
            return null;
        } else {
            return null;
        }
    }

    @ApiOperation(value = "假删除文件")
    @PostMapping("/delete")
    public AjaxResult delFiles(@RequestBody FileDO fileDO) {
        try {
            fileDO.setDelFlag("1");
            fileDO.setFileDelDate(minioUtils.getDatePath());
            fileService.updateFileDOById(fileDO);
            return AjaxResult.success("删除文件成功");
        } catch (Exception e) {
            return AjaxResult.error("删除文件失败", e);
        }
    }

    @ApiOperation(value = "删除多个文件")
    @PostMapping("/delete/Batch")
    public AjaxResult delFiles(@RequestBody SelectFileParam selectFileParam) {
        List<FileDO> userFiles = JSON.parseArray(selectFileParam.getFiles(), FileDO.class);
        for (FileDO file : userFiles) {
            file.setDelFlag("1");
            file.setFileDelDate(minioUtils.getDatePath());
            fileService.updateById(file);
        }
        return AjaxResult.success("删除成功");
    }


    @ApiOperation(value = "文件重命名")
    @PostMapping("/rename")
    public AjaxResult FileRename(@RequestBody FileDO fileDO) {
        // getByFileName查询  当前文件夹下是否有重名
        FileDO file = fileService.selectFileDOByParams(fileDO);
        // 有--结束
        if (StringUtils.isNotNull(file) && fileDO.getIsDirectory().equals(file.getIsDirectory())) {
            if ("1".equals(fileDO.getIsDirectory())) {
                return AjaxResult.error("同名文件夹已存在");
            } else {
                return AjaxResult.error("同名文件已存在");
            }
        } else {
            fileDO.setUpdateBy(SecurityUtils.getUsername());
            fileDO.setUpdateTime(DateUtils.getNowDate());
            fileService.updateFileDOById(fileDO);
            return AjaxResult.success("重命名成功");
        }
    }

    /**
     * 彻底删除回收站里的某个对象
     *
     * @param fileUpdateParam 对象
     * @return
     */
    @PostMapping(value = "/delFile")
    @ApiOperation(value = "彻底删除文件")
    public AjaxResult delFile(FileUpdateParam fileUpdateParam) {
        FileDO delFile = fileService.getById(fileUpdateParam.getId());
        fileService.removeById(fileUpdateParam.getId());
        if (minioUtils.delete(bucketName, delFile.getFileInbuckName())) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除失败");
        }
    }

    /**
     * 清空回收站
     *
     * @return
     */
    @GetMapping(value = "/EmptyTrash")
    @ApiOperation(value = "清空回收站")
    public AjaxResult delFiles() {
        LambdaQueryWrapper<FileDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FileDO::getBucketName, bucketName).isNotNull(FileDO::getFileDelDate);
        fileService.remove(lambdaQueryWrapper);
        return AjaxResult.success("清空回收站成功");
    }

    /**
     * 还原回收站
     *
     * @return
     */
    @GetMapping(value = "/restoreTrash")
    @ApiOperation(value = "还原回收站")
    public AjaxResult restoreTrash() {
        fileService.restoreTrash(bucketName);
        return AjaxResult.success("还原回收站成功");
    }

    /**
     * 还原回收站多个文件
     *
     * @param selectFileParam
     * @return
     */
    @ApiOperation(value = "还原多个文件")
    @PostMapping("/Restore/Batch")
    public AjaxResult reFiles(@RequestBody SelectFileParam selectFileParam) {
        List<FileDO> userFiles = JSON.parseArray(selectFileParam.getFiles(), FileDO.class);
        for (FileDO file : userFiles) {
            file.setDelFlag("0");
            fileService.setDelTimeNull(file.getId());
            file.setFileDelDate(null);
//            file.setFileDelDate("");
            fileService.updateById(file);
        }
        return AjaxResult.success("还原文件成功");
    }

    /**
     * 彻底删除回收站多个文件
     *
     * @param selectFileParam
     * @return
     */
    @ApiOperation(value = "彻底删除多个文件")
    @PostMapping("/Empty/Batch")
    public AjaxResult dellsFiles(@RequestBody SelectFileParam selectFileParam) {
        List<FileDO> userFiles = JSON.parseArray(selectFileParam.getFiles(), FileDO.class);
        for (FileDO file : userFiles) {
            if ("0".equals(file.getIsDirectory())) {
                minioUtils.delete(bucketName, file.getFileInbuckName());
            }
            fileService.removeById(file.getId());
        }
        return AjaxResult.success("彻底删除文件成功");
    }


    @ApiOperation(value = "获取文件树")
    @GetMapping("/getTree")
    @ResponseBody
    public AjaxResult getFileTree() {
        List<FileDO> filePathList = fileService.selectFilePathTreeByBucket(bucketName);
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        resultTreeNode.setLabel("/");

        for (int i = 0; i < filePathList.size(); i++) {
            String filePath = filePathList.get(i).getFilePath() + filePathList.get(i).getFileName() + "/";

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (int j = 0; j < strArr.length; j++) {
                if (!"".equals(strArr[j]) && strArr[j] != null) {
                    queue.add(strArr[j]);
                }

            }
            if (queue.size() == 0) {
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode, "/", queue);

        }
//        result.setSuccess(true);
//        result.setData(resultTreeNode);
        return AjaxResult.success("获取成功", resultTreeNode);

    }

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue) {

        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }

        Map<String, String> map = new HashMap<>();
        filePath = filePath + currentNodeName + "/";
        map.put("filePath", filePath);

        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();


            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);

            childrenTreeNodes.add(resultTreeNode);

        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {

                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }
            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path) {
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                if (path.equals(childrenTreeNodes.get(i).getLabel())) {
                    isExistPath = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return isExistPath;
    }

    @ApiOperation(value = "文件移动")
    @PostMapping("/move")
    public AjaxResult moveFiles(FileUpdateParam fileUpdateParam) {
        FileDO fileUpUrl = fileService.getById(fileUpdateParam.getId());
        String fileURL = fileUpUrl.getFilePath();
        fileUpUrl.setFilePath(fileUpdateParam.getFileUrl());
        if ("1".equals(fileUpUrl.getIsDirectory())) {
            String fileOldPath = fileURL + fileUpUrl.getFileName() + "/";
            String fileNewPath = fileUpdateParam.getFileUrl() + fileUpUrl.getFileName() + "/";
            fileService.updateFilePath(bucketName, fileOldPath, fileNewPath);

        }
        fileService.updateById(fileUpUrl);
        return AjaxResult.success("文件移动成功");
    }

    @ApiOperation(value = "批量文件移动")
    @PostMapping("/move/Batch")
    public AjaxResult BatchMoveFiles(@RequestBody SelectFileParam selectFileParam) {
        List<FileDO> userFiles = JSON.parseArray(selectFileParam.getFiles(), FileDO.class);
        for (FileDO file : userFiles) {
            if ("1".equals(file.getIsDirectory())) {
                String fileOldPath = file.getFilePath() + file.getFileName() + "/";
                String fileNewPath = selectFileParam.getFilePath() + file.getFileName() + "/";
                fileService.updateFilePath(bucketName, fileOldPath, fileNewPath);
            }
            file.setFilePath(selectFileParam.getFilePath());
            fileService.updateById(file);
        }
        return AjaxResult.success("文件移动成功");
    }

    @PostMapping("/download/{fileName}")
    public AjaxResult download(HttpServletResponse response, @PathVariable("fileName")
            String fileName) {
        String url = downLoadPath;
        String result = "";
        try {
            result = minioUtils.downloadObject(bucketName, fileName, url + fileName);
        } catch (Exception e) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success(result);
    }

    /*@GetMapping("/list")
    public AjaxResult list() throws Exception {
        Iterable<Result<Item>> list = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = list.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName': '%s', 'fileSize': '%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse(String.format(format, item.objectName(), MinioUtils.formatFileSize(item.size()))));
        }
        return AjaxResult.success(items);
    }

    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam(name="file", required = false) MultipartFile[] file) {
        if (file == null || file.length == 0) {
            return AjaxResult.error("上传文件不能为空");
        }
        List<String> stringList = minioUtils.putObject(file, bucketName);
        Map<String, Object> map = new HashMap<>();
        map.put("bucketName", bucketName);
        map.put("fileName", stringList);
        patientService.uploadFile(file);
        return AjaxResult.success("上传成功", map);
    }

    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestParam(name="file", required = false) MultipartFile file) throws Exception{
        MinioResponse minioResponse = minioUtils.uploadFile(file, bucketName);
        return AjaxResult.success(minioResponse.getMinioUrl());
    }

    *//**
     * 下载对象
     * @param response
     * @param fileName
     * @return
     *//*
    @PostMapping("/download/{fileName}")
    public AjaxResult download(HttpServletResponse response, @PathVariable("fileName")
            String fileName) {
        String url = "D:\\Download\\miniodown\\";
        String result = "";
        try {
            result = minioUtils.downloadObject(bucketName, fileName, url + fileName);
        } catch (Exception e) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success(result);
    }

    *//***
     * 删除
     * @param fileName
     * @return
     *//*
    @DeleteMapping("/delete/{fileName}")
    public AjaxResult delete(@PathVariable("fileName") String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build()
            );
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success("删除成功");
    }*/

}
