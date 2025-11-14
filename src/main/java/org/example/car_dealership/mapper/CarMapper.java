package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.service.S3Service;
import org.springframework.stereotype.Component;

import static org.example.car_dealership.model.config.car.ImageVariant.THUMB;

@Component
public class CarMapper {

    private final S3Service s3Service;

    public CarMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public CarListItemDto toCarListDto(Car car) {
        CarListItemDto dto = new CarListItemDto();
        dto.setId(car.getId());
        dto.setTitle(car.getBrand() + " " + car.getModel());
        dto.setType(car.getType());
        dto.setPrice(car.getPrice());
        String thumbUrl = null;
        if (car.getCarImages() != null && !car.getCarImages().isEmpty()) {
            thumbUrl = car.getCarImages().stream()
                    .filter(img -> THUMB.equals(img.getImageVariant()) && img.isPrimary())
                    .findFirst()
                    .map(CarImage::getStorageKey)
                    .map(s3Service::generatePresignedUrl) // ← ДОДАЄМО ГЕНЕРАЦІЮ URL
                    .orElse(null);
        }

        dto.setThumbUrl(thumbUrl);
        return dto;
    }
}
