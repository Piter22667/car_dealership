package org.example.car_dealership.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface S3Service {

    String uploadMultipartFile(String key, MultipartFile file);

    String generatePresignedUrl(String key);

}
