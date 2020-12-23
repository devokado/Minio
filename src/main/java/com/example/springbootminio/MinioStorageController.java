package com.example.springbootminio;

import com.google.api.client.util.IOUtils;
import io.minio.ObjectStat;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MinioStorageController {
    @Autowired
    MinioAdapter minioAdapter;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioAdapter.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
      minioAdapter.UploadFile(files.getOriginalFilename(), files.getBytes());
      Map<String,String> result = new HashMap<>();
      result.put("key",files.getOriginalFilename());
      return result;
    }

    @GetMapping(path = "/download/{file}")
    public ResponseEntity<ByteArrayResource> DownloadFile(@PathVariable String file) throws IOException {
          byte[] data = minioAdapter.getFile(file);
          ByteArrayResource resource = new ByteArrayResource(data);

          return ResponseEntity
                  .ok()
                  .contentLength(data.length)
                  .header("Content-type", "application/octet-stream")
                  .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                  .body(resource);

    }

    @GetMapping("/")
    public List<Item> testMinio()  {
        return minioAdapter.list();
    }


    @GetMapping("/remove/{file}")
    public void RemoveFile(@PathVariable String file) throws IOException {
        minioAdapter.removeFile(file);
    }

    @GetMapping("/health")
    public Health health()
    {
        MinioHealthIndicator minioHealthIndicator = new MinioHealthIndicator();
        return minioHealthIndicator.health();
    }

}