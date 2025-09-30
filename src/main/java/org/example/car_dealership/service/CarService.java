package org.example.car_dealership.service;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface CarService {
    Page<CarListItemDto> getCarList(Pageable pageable);

    CarDetailsDto getCarById(Long id);

//    CreateCarDto createCar(CreateCarDto createCarDto);


}
