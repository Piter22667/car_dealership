package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.CarImageResponseDto;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.service.S3Service;
import org.springframework.stereotype.Component;

@Component
public class CarImageMapper {

    private final S3Service s3Service;

    public CarImageMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public CarImageResponseDto toDto(CarImage carImage){
        CarImageResponseDto carImageResponseDto = new CarImageResponseDto();
        carImageResponseDto.setStorageKey(carImage.getStorageKey());
        carImageResponseDto.setUrl(s3Service.generatePresignedUrl(carImage.getStorageKey()));
        carImageResponseDto.setPrimary(carImage.isPrimary());
        return carImageResponseDto;
    }
}
