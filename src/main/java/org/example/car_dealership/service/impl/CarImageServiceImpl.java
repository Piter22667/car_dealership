package org.example.car_dealership.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.CarImageResponseDto;
import org.example.car_dealership.exception.CarImageNotFoundException;
import org.example.car_dealership.exception.InvalidFileFormatException;
import org.example.car_dealership.exception.InvalidImageCountException;
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

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<CarImageResponseDto> uploadImages(Long carId, MultipartFile thumbnail, List<MultipartFile> originImages) {
        // Валідація
        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new InvalidImageCountException("Thumbnail image is required");
        }

        if (originImages == null || originImages.isEmpty()) {
            throw new InvalidImageCountException("At least 1 origin image is required");
        }

        if (originImages.size() > 5) {
            throw new InvalidImageCountException("Maximum 5 origin images allowed");
        }

        // Валідація форматів файлів
        validateImageFormat(thumbnail);
        originImages.forEach(this::validateImageFormat);

        List<CarImageResponseDto> uploadedImages = new ArrayList<>();

        // Завантажуємо thumbnail
        CarImageResponseDto thumbResponse = uploadImage(carId, thumbnail, ImageVariant.THUMB, true);
        uploadedImages.add(thumbResponse);
        log.info("Thumbnail image uploaded for car {}", carId);

        // Завантажуємо origin зображення
        for (int i = 0; i < originImages.size(); i++) {
            boolean isPrimary = (i == 0); // Перше зображення буде primary
            CarImageResponseDto originResponse = uploadImage(carId, originImages.get(i), ImageVariant.ORIGIN, isPrimary);
            uploadedImages.add(originResponse);
        }

        log.info("Successfully uploaded {} images for car {}", uploadedImages.size(), carId);
        return uploadedImages;
    }

    private void validateImageFormat(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileFormatException("Image file is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") &&
            !contentType.equals("image/png"))) {
            throw new InvalidFileFormatException("Only JPEG and PNG formats are allowed");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "jpg";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "jpg";
    }

    private String getVariantFolder(ImageVariant variant) {
        return switch (variant) {
            case THUMB -> "thumb";
            case ORIGIN -> "origin";
        };
    }
}
