package com.ruoyi.web.core.config;

import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author diaozhiqiang
 * @description 描述：
 * @date 2022年07月25日
 */
@Component
public class MinioConfig {

    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean(PearlMinioClient.class)
    public PearlMinioClient minioClient(MinioProperties minioProperties) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return new PearlMinioClient(minioClient);
    }

}
