package com.example.springbootminio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;



@ConditionalOnClass(ManagementContextAutoConfiguration.class)
@Component
public class MinioHealthIndicator implements HealthIndicator {

    @Autowired
    private MinioClient minioClient;

   @Autowired
   private MinioProperties minioProperties;



    @Override
    public Health health() {
        if (minioClient == null) {
            return Health.down().build();
        }

        try {
            if (minioClient.bucketExists(minioProperties.getBuckek_name())) {
                return Health.up()
                        .withDetail("bucketName",minioProperties.getBuckek_name())
                        .build();
            } else {
                return Health.down()
                        .withDetail("bucketName", minioProperties.getBuckek_name())
                        .build();
            }
        } catch (Exception e) {
            return Health.down(e)
                    .withDetail("bucketName",minioProperties.getBuckek_name())
                    .build();
        }
    }
}