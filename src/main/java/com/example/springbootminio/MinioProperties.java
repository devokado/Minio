package com.example.springbootminio;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties("spring.minio")
public class MinioProperties {

    private  String  buckek_name="test";
    private  String  default_folder="/";
    private  String  access_key="AKIAIOSFODNN7EXAMPLE";
    private   String   access_secret="wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private   String   url="http://127.0.0.1:9000";

    private String metricName = "minio.storage";


    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration writeTimeout = Duration.ofSeconds(60);
    private Duration readTimeout = Duration.ofSeconds(10);

    public String getBuckek_name() {
        return buckek_name;
    }

    public void setBuckek_name(String buckek_name) {
        this.buckek_name = buckek_name;
    }

    public String getDefault_folder() {
        return default_folder;
    }

    public void setDefault_folder(String default_folder) {
        this.default_folder = default_folder;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getAccess_secret() {
        return access_secret;
    }

    public void setAccess_secret(String access_secret) {
        this.access_secret = access_secret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }


}
