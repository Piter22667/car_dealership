package org.example.car_dealership.controller;

import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/list")
    public Page<CarListItemDto> getCars(Pageable pageable) {
        return carService.getCarList(pageable);
    }

    @GetMapping("/{id}")
    public CarDetailsDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }
}
