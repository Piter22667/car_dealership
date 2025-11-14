package org.example.car_dealership.service;

import org.example.car_dealership.dto.CarImageResponseDto;
import org.example.car_dealership.model.config.car.ImageVariant;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CarImageService {
    CarImageResponseDto uploadImage(Long carId, MultipartFile image, ImageVariant variant, boolean isPrimary);
}
