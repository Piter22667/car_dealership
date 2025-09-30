package org.example.car_dealership.service.impl;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Page<CarListItemDto> getCarList(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(car -> {
                    CarListItemDto dto = new CarListItemDto();
                    dto.setId(car.getId());
                    dto.setTitle(car.getBrand() + " " + car.getModel());
                    dto.setType(car.getType());
                    dto.setYear(car.getYear());
                    dto.setPrice(car.getPrice());
                    dto.setMileage(car.getMileage());
                    dto.setImageUrl(
                            car.getCarImages() != null && !car.getCarImages().isEmpty()
                                    ? car.getCarImages().get(0).getImageUrl()
                                    : null
                    );
                    return dto;
                });
    }

    @Override
    public CarDetailsDto getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found") {
                });
        return toDetailsCarDto(car);
    }

    private CarDetailsDto toDetailsCarDto(Car car) {
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
        dto.setImageUrls(
                car.getCarImages() != null
                        ? car.getCarImages().stream().map(CarImage::getImageUrl).toList()
                        : List.of()
        );
        return dto;
    }
}
