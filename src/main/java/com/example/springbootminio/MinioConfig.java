package com.example.springbootminio;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;


    @Bean
    public MinioClient generateMinioClient(){
        MinioClient client;
        try{
            client = new MinioClient(minioProperties.getUrl(),minioProperties.getAccess_key(),minioProperties.getAccess_secret());
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
        try {
            boolean b = client.bucketExists(minioProperties.getBuckek_name());
            if (!b) {
                    try {
                        client.makeBucket(minioProperties.getBuckek_name());
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot create bucket", e);
                    }
                }

        }
        catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }
        return client;
    }
}
