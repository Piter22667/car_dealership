package org.example.car_dealership.service.impl;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.example.car_dealership.dto.UpdateCarRequestDto;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.model.config.car.FuelType;
import org.example.car_dealership.model.config.car.Interior;
import org.example.car_dealership.model.config.car.Transmission;
import org.example.car_dealership.model.config.car.Type;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
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

    @Override
    public CarDetailsDto createCar(CreateCarRequestDto createCarRequestDto) {
        Car car = toCarEntity(createCarRequestDto);
        Car savedCar = carRepository.save(car);
        return toDetailsCarDto(savedCar);
    }

    @Override
    public CarDetailsDto updateCar(Long id, UpdateCarRequestDto updateCarRequestDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        updateCarFromDto(car, updateCarRequestDto);
        Car updatedCar = carRepository.save(car);
        return toDetailsCarDto(updatedCar);
    }

    @Override
    public void deleteCar(Long id) {
        if(!carRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
        }
        carRepository.deleteById(id);
    }

    private void updateCarFromDto(Car car, UpdateCarRequestDto dto) {
        if (dto.getBrand() != null) car.setBrand(dto.getBrand());
        if (dto.getModel() != null) car.setModel(dto.getModel());
        if (dto.getManufacturer() != null) car.setManufacturer(dto.getManufacturer());
        if (dto.getRegistrationNumber() != null) car.setRegistrationNumber(dto.getRegistrationNumber());
        if (dto.getEngineVolume() != null) car.setEngineVolume(dto.getEngineVolume());
        if (dto.getEnginePower() != null) car.setEnginePower(dto.getEnginePower());
        if (dto.getFuelConsumption() != null) car.setFuelConsumption(dto.getFuelConsumption());
        if (dto.getDoors() != null) car.setDoors(dto.getDoors());
        if (dto.getSeats() != null) car.setSeats(dto.getSeats());
        if (dto.getTrunkCapacity() != null) car.setTrunkCapacity(dto.getTrunkCapacity());
        if (dto.getTransmission() != null) car.setTransmission(Transmission.valueOf(dto.getTransmission().toUpperCase()));
        if (dto.getCruiseControl() != null) car.setCruiseControl(dto.getCruiseControl());
        if (dto.getFuelType() != null) car.setFuelType(FuelType.valueOf(dto.getFuelType().toUpperCase()));
        if (dto.getMileage() != null) car.setMileage(dto.getMileage());
        if (dto.getLastServiceDate() != null) car.setLastServiceDate(dto.getLastServiceDate());
        if (dto.getPrice() != null) car.setPrice(dto.getPrice());
        if (dto.getColor() != null) car.setColor(dto.getColor());
        if (dto.getInterior() != null) car.setInterior(Interior.valueOf(dto.getInterior().toUpperCase()));
        if (dto.getType() != null) car.setType(Type.valueOf(dto.getType().toUpperCase()));
        if (dto.getYear() != null) car.setYear(dto.getYear());

        if (dto.getImageUrls() != null) {
            // This approach replaces all old images with the new ones.
            car.getCarImages().clear();
            List<CarImage> carImages = dto.getImageUrls().stream()
                    .map(url -> {
                        CarImage carImage = new CarImage();
                        carImage.setImageUrl(url);
                        carImage.setCar(car);
                        return carImage;
                    })
                    .toList();
            car.getCarImages().addAll(carImages);
        }
    }

    private Car toCarEntity(CreateCarRequestDto createCarRequestDto) {
        Car car = new Car();
        car.setBrand(createCarRequestDto.getBrand());
        car.setModel(createCarRequestDto.getModel());
        car.setManufacturer(createCarRequestDto.getManufacturer());
        car.setRegistrationNumber(createCarRequestDto.getRegistrationNumber());
        car.setEngineVolume(createCarRequestDto.getEngineVolume());
        car.setEnginePower(createCarRequestDto.getEnginePower());
        car.setFuelConsumption(createCarRequestDto.getFuelConsumption());
        car.setDoors(createCarRequestDto.getDoors());
        car.setSeats(createCarRequestDto.getSeats());
        car.setTrunkCapacity(createCarRequestDto.getTrunkCapacity());
        car.setTransmission(createCarRequestDto.getTransmission() != null ?
                Transmission.valueOf(createCarRequestDto.getTransmission().toUpperCase()) : null);
        car.setCruiseControl(createCarRequestDto.getCruiseControl());
        car.setFuelType(FuelType.valueOf(createCarRequestDto.getFuelType().toUpperCase()));
        car.setMileage(createCarRequestDto.getMileage());
        car.setLastServiceDate(createCarRequestDto.getLastServiceDate());
        car.setPrice(createCarRequestDto.getPrice());
        car.setColor(createCarRequestDto.getColor());
        car.setInterior(Interior.valueOf(createCarRequestDto.getInterior().toUpperCase()));
        car.setType(Type.valueOf(createCarRequestDto.getType().toUpperCase()));
        car.setYear(createCarRequestDto.getYear());
        if (createCarRequestDto.getImageUrls() != null) {
            List<CarImage> images = createCarRequestDto.getImageUrls().stream()
                    .map(url -> {
                        CarImage image = new CarImage();
                        image.setImageUrl(url);
                        image.setCar(car);
                        return image;
                    })
                    .toList();
            car.setCarImages(images);
        }
        return car;
    }
}
