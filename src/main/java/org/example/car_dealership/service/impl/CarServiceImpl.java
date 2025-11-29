package org.example.car_dealership.service.impl;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.exception.CarNotExistException;
import org.example.car_dealership.mapper.CarMapper;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.service.CarService;
import org.example.car_dealership.service.S3Service;
import org.example.car_dealership.specification.CarSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final S3Service s3Service;
    private final CarMapper carMapper;
    private final CarSpecification carSpecification;

    public CarServiceImpl(CarRepository carRepository, S3Service s3Service, CarMapper carMapper, CarSpecification carSpecification) {
        this.carRepository = carRepository;
        this.s3Service = s3Service;
        this.carMapper = carMapper;
        this.carSpecification = carSpecification;
    }

    @Override
    public Page<CarListItemDto> getCarList(Pageable pageable, CarFilterDto carFilterDto) {
        Specification<Car> specification = Specification.allOf(
                        carSpecification.brandLike(carFilterDto.getBrand()))
                .and(carSpecification.minPrice(carFilterDto.getMinPrice()))
                .and(carSpecification.maxPrice(carFilterDto.getMaxPrice()))
                .and(carSpecification.typeLike(carFilterDto.getType()));

        Page<Car> page = carRepository.findAll(specification, pageable);


        return page.map(car -> carMapper.toCarListDto(car));
    }

    @Override
    public CarDetailsDto getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotExistException("Car not found with given id."));
        return carMapper.toDetailsCarDto(car);
    }


//    @Override
//    public CarDetailsDto createCar(CreateCarRequestDto createCarRequestDto) {
//        Car car = toCarEntity(createCarRequestDto);
//        Car savedCar = carRepository.save(car);
//        return toDetailsCarDto(savedCar);
//    } //TODO finish create car method

//    @Override
//    public CarDetailsDto updateCar(Long id, UpdateCarRequestDto updateCarRequestDto) {
//        Car car = carRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
//
//        updateCarFromDto(car, updateCarRequestDto);
//        Car updatedCar = carRepository.save(car);
//        return toDetailsCarDto(updatedCar);
//    } //TODO finish update car method

//    @Override
//    public void deleteCar(Long id) {
//        if (!carRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
//        }
//        carRepository.deleteById(id);
//    } //TODO finish delete car mehtod

//    private void updateCarFromDto(Car car, UpdateCarRequestDto dto) {
//        if (dto.getBrand() != null) car.setBrand(dto.getBrand());
//        if (dto.getModel() != null) car.setModel(dto.getModel());
//        if (dto.getManufacturer() != null) car.setManufacturer(dto.getManufacturer());
//        if (dto.getRegistrationNumber() != null) car.setRegistrationNumber(dto.getRegistrationNumber());
//        if (dto.getEngineVolume() != null) car.setEngineVolume(dto.getEngineVolume());
//        if (dto.getEnginePower() != null) car.setEnginePower(dto.getEnginePower());
//        if (dto.getFuelConsumption() != null) car.setFuelConsumption(dto.getFuelConsumption());
//        if (dto.getDoors() != null) car.setDoors(dto.getDoors());
//        if (dto.getSeats() != null) car.setSeats(dto.getSeats());
//        if (dto.getTrunkCapacity() != null) car.setTrunkCapacity(dto.getTrunkCapacity());
//        if (dto.getTransmission() != null)
//            car.setTransmission(Transmission.valueOf(dto.getTransmission().toUpperCase()));
//        if (dto.getCruiseControl() != null) car.setCruiseControl(dto.getCruiseControl());
//        if (dto.getFuelType() != null) car.setFuelType(FuelType.valueOf(dto.getFuelType().toUpperCase()));
//        if (dto.getMileage() != null) car.setMileage(dto.getMileage());
//        if (dto.getLastServiceDate() != null) car.setLastServiceDate(dto.getLastServiceDate());
//        if (dto.getPrice() != null) car.setPrice(dto.getPrice());
//        if (dto.getColor() != null) car.setColor(dto.getColor());
//        if (dto.getInterior() != null) car.setInterior(Interior.valueOf(dto.getInterior().toUpperCase()));
//        if (dto.getType() != null) car.setType(Type.valueOf(dto.getType().toUpperCase()));
//        if (dto.getYear() != null) car.setYear(dto.getYear());

//        if (dto.getImageUrls() != null) {
//            car.getCarImages().clear();
//            List<CarImage> carImages = dto.getImageUrls().stream()
//                    .map(url -> {
//                        CarImage carImage = new CarImage();
//                        carImage.setImageUrl(url);
//                        carImage.setCar(car);
//                        return carImage;
//                    })
//                    .toList();
//            car.getCarImages().addAll(carImages);
//        }
}

//    private Car toCarEntity(CreateCarRequestDto createCarRequestDto) {
//        Car car = new Car();
//        car.setBrand(createCarRequestDto.getBrand());
//        car.setModel(createCarRequestDto.getModel());
//        car.setManufacturer(createCarRequestDto.getManufacturer());
//        car.setRegistrationNumber(createCarRequestDto.getRegistrationNumber());
//        car.setEngineVolume(createCarRequestDto.getEngineVolume());
//        car.setEnginePower(createCarRequestDto.getEnginePower());
//        car.setFuelConsumption(createCarRequestDto.getFuelConsumption());
//        car.setDoors(createCarRequestDto.getDoors());
//        car.setSeats(createCarRequestDto.getSeats());
//        car.setTrunkCapacity(createCarRequestDto.getTrunkCapacity());
//        car.setTransmission(createCarRequestDto.getTransmission() != null ?
//                Transmission.valueOf(createCarRequestDto.getTransmission().toUpperCase()) : null);
//        car.setCruiseControl(createCarRequestDto.getCruiseControl());
//        car.setFuelType(FuelType.valueOf(createCarRequestDto.getFuelType().toUpperCase()));
//        car.setMileage(createCarRequestDto.getMileage());
//        car.setLastServiceDate(createCarRequestDto.getLastServiceDate());
//        car.setPrice(createCarRequestDto.getPrice());
//        car.setColor(createCarRequestDto.getColor());
//        car.setInterior(Interior.valueOf(createCarRequestDto.getInterior().toUpperCase()));
//        car.setType(Type.valueOf(createCarRequestDto.getType().toUpperCase()));
//        car.setYear(createCarRequestDto.getYear());
//        if (createCarRequestDto.getImageUrls() != null) {
//            List<CarImage> images = createCarRequestDto.getImageUrls().stream()
//                    .map(url -> {
//                        CarImage image = new CarImage();
//                        image.setImageUrl(url);
//                        image.setCar(car);
//                        return image;
//                    })
//                    .toList();
//            car.setCarImages(images);
//        }
//        return car;
//    }

