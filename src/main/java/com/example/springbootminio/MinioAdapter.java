package com.example.springbootminio;

import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

@Service
public class MinioAdapter {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    public List<Bucket> getAllBuckets()
    {
        try{
            return minioClient.listBuckets();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public void UploadFile(String name,byte[] content){
        File file = new File("/tmp/"+name);
        file.canWrite();
        file.canRead();
        try {
            FileOutputStream iofs = new FileOutputStream(file);
            iofs.write(content);
            minioClient.putObject(minioProperties.getBuckek_name(), minioProperties.getDefault_folder()+name,file.getAbsolutePath());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public byte[] getFile(String key){
        try {
            InputStream obj = minioClient.getObject(minioProperties.getBuckek_name(),minioProperties.getDefault_folder()+"/"+key);
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeFile(String key){
        try {
            minioClient.removeObject(minioProperties.getBuckek_name(),minioProperties.getDefault_folder()+"/"+key);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}
