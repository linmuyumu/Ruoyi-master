package com.ruoyi.web.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LinMu
 * @description 描述：
 * @date 2022年06月07日
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 端点
     */
    private String endpoint;

    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 开启https
     */
    private Boolean secure;

}
