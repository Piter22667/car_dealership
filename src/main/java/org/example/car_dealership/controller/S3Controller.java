package org.example.car_dealership.controller;

import org.example.car_dealership.dto.CarImageResponseDto;
import org.example.car_dealership.model.config.car.ImageVariant;
import org.example.car_dealership.service.impl.CarImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final CarImageServiceImpl carImageService;

    public S3Controller(CarImageServiceImpl carImageService) {
        this.carImageService = carImageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<CarImageResponseDto> uploadImage(@RequestParam Long carId, @RequestParam MultipartFile image, @RequestParam ImageVariant variant, @RequestParam boolean isPrimary) {
        CarImageResponseDto carImage = carImageService.uploadImage(carId, image, variant, isPrimary);
        return ResponseEntity.ok(carImage);
    }
}
