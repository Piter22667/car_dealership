package org.example.car_dealership.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.CarImageResponseDto;
import org.example.car_dealership.exception.CarImageNotFoundException;
import org.example.car_dealership.mapper.CarImageMapper;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.model.config.car.ImageVariant;
import org.example.car_dealership.repository.CarImageRepository;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.service.CarImageService;
import org.example.car_dealership.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Slf4j
public class CarImageServiceImpl implements CarImageService {

    private final S3Service s3Service;
    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;
    private final CarImageMapper carImageMapper;

    public CarImageServiceImpl(S3Service s3Service, CarRepository carRepository, CarImageRepository carImageRepository, CarImageMapper carImageMapper) {
        this.s3Service = s3Service;
        this.carRepository = carRepository;
        this.carImageRepository = carImageRepository;
        this.carImageMapper = carImageMapper;
    }


    @Override
    public CarImageResponseDto uploadImage(Long carId, MultipartFile image, ImageVariant imageVariant, boolean isPrimary) {

        Car car = carRepository.findById(carId).orElseThrow(
                () -> new CarImageNotFoundException("Car image not found"));

        log.debug("car found with id {}", car.getId());

        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(image.getOriginalFilename());
        String imgVariant = getVariantFolder(imageVariant);
        String key = String.format("cars/%d/%s/%s.%s", carId, imgVariant, uuid, extension);

        String storageKey = s3Service.uploadMultipartFile(key, image);

        log.info("Image uploaded to S3 docker localstack with key: {}", storageKey);


        CarImage carImage = CarImage.builder()
                .car(car)
                .storageKey(storageKey)
                .imageVariant(imageVariant)
                .isPrimary(isPrimary)
                .build();

        carImageRepository.save(carImage);

        return carImageMapper.toDto(carImage);

    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "jpg";
    }

    private String getVariantFolder(ImageVariant variant) {
        return switch (variant) {
            case THUMB -> "thumb";
            case ORIGIN -> "origin";
            default -> "unknown";
        };
    }
}
