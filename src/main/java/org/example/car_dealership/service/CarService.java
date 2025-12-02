package org.example.car_dealership.service;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public interface CarService {
    Page<CarListItemDto> getCarList(Pageable pageable, CarFilterDto carFilterDto);

    CarDetailsDto getCarById(Long id);

    CarDetailsDto createCar(CreateCarRequestDto createCarRequestDto, MultipartFile thumbnail, List<MultipartFile> originImages);

//    CarDetailsDto updateCar(Long id, UpdateCarRequestDto updateCarRequestDto);

//    void deleteCar(Long id);

}
