package org.example.car_dealership.mapper;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.service.S3Service;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.car_dealership.model.config.car.ImageVariant.ORIGIN;
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

    public CarDetailsDto toDetailsCarDto(Car car) {
        CarDetailsDto dto = new CarDetailsDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setManufacturer(car.getManufacturer());
        dto.setRegistrationNumber(car.getRegistrationNumber());
        dto.setEngineVolume(car.getEngineVolume());
        dto.setEnginePower(car.getEnginePower());
        dto.setFuelConsumption(car.getFuelConsumption());
        dto.setDoors(car.getDoors());
        dto.setSeats(car.getSeats());
        dto.setTrunkCapacity(car.getTrunkCapacity());
        dto.setTransmission(car.getTransmission() != null ? car.getTransmission().toString() : null);
        dto.setCruiseControl(car.getCruiseControl());
        dto.setFuelType(car.getFuelType() != null ? car.getFuelType().toString() : null);
        dto.setMileage(car.getMileage());
        dto.setLastServiceDate(car.getLastServiceDate());
        dto.setPrice(car.getPrice());
        dto.setColor(car.getColor());
        dto.setInterior(car.getInterior() != null ? car.getInterior().toString() : null);
        dto.setType(car.getType());
        dto.setYear(car.getYear());
        List<String> imageUrls = car.getCarImages() == null ? Collections.emptyList()
                : car.getCarImages().stream()
                .filter(Objects::nonNull)
                .filter(img -> ORIGIN.equals(img.getImageVariant()))
                .sorted(Comparator.comparing(CarImage::isPrimary).reversed())
                .map(CarImage::getStorageKey)
                .filter(Objects::nonNull)
                .map(s3Service::generatePresignedUrl)
                .collect(Collectors.toList());
        dto.setImageUrls(imageUrls);
        return dto;
    }

    public Car toEntity(CreateCarRequestDto dto) {
        return Car.builder()
                .brand(dto.getBrand())
                .model(dto.getModel())
                .manufacturer(dto.getManufacturer())
                .registrationNumber(dto.getRegistrationNumber())
                .engineVolume(dto.getEngineVolume())
                .enginePower(dto.getEnginePower())
                .fuelConsumption(dto.getFuelConsumption())
                .doors(dto.getDoors())
                .seats(dto.getSeats())
                .trunkCapacity(dto.getTrunkCapacity())
                .transmission(dto.getTransmission())
                .cruiseControl(dto.getCruiseControl())
                .fuelType(dto.getFuelType())
                .mileage(dto.getMileage())
                .lastServiceDate(dto.getLastServiceDate())
                .price(dto.getPrice())
                .color(dto.getColor())
                .interior(dto.getInterior())
                .type(dto.getType())
                .year(dto.getYear())
                .build();
    }
}
