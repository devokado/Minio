package com.example.springbootminio;


import io.minio.MinioClient;
import io.minio.errors.InvalidBucketNameException;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfig.class);

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient;
        try {
            if(!configuredProxy()) {
                minioClient = new MinioClient(
                        minioProperties.getUrl(),
                        minioProperties.getAccess_key(),
                        minioProperties.getAccess_secret(),
                        minioProperties.isSecure()
                );
            }
            else{
                minioClient = new MinioClient(
                        minioProperties.getUrl(),
                        0,
                        minioProperties.getAccess_key(),
                        minioProperties.getAccess_secret(),
                        null,
                        minioProperties.isSecure(),
                        client()
                );
            }
            minioClient.setTimeout(
                    minioProperties.getConnectTimeout().toMillis(),
                    minioProperties.getWriteTimeout().toMillis(),
                    minioProperties.getReadTimeout().toMillis()
            );
        } catch (Exception e) {
            LOGGER.error("Error while connecting to Minio", e);
            throw new RuntimeException(e);
        }

        if (minioProperties.isCheckBucket()) {
            try {
                LOGGER.debug("Checking if bucket {} exists", minioProperties.getBuckek_name());
                boolean b = minioClient.bucketExists(minioProperties.getBuckek_name());
                if (!b) {
                    if (minioProperties.isCreateBucket()) {
                        try {
                            minioClient.makeBucket(minioProperties.getBuckek_name());
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot create bucket", e);
                        }
                    } else {
                        throw new InvalidBucketNameException(minioProperties.getBuckek_name(), "Bucket does not exists");
                    }
                }
            } catch
            (Exception e) {
                LOGGER.error("Error while checking bucket", e);
                throw new RuntimeException(e);
            }
        }

        return minioClient;
    }

    private boolean configuredProxy(){
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");
        return httpHost!=null && httpPort!=null;
    }
    private OkHttpClient client() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(httpHost!=null)
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpHost,Integer.parseInt(httpPort))));
        return builder
                .build();
    }

}