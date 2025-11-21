package org.example.car_dealership.service;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface CarService {
    Page<CarListItemDto> getCarList(Pageable pageable, CarFilterDto carFilterDto);

    CarDetailsDto getCarById(Long id);

//    CarDetailsDto createCar(CreateCarRequestDto createCarRequestDto);

//    CarDetailsDto updateCar(Long id, UpdateCarRequestDto updateCarRequestDto);

//    void deleteCar(Long id);

}
