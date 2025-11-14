package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarController implements CarControllerInterface {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Parameter(name = "page", in = ParameterIn.QUERY)
    @Parameter(name = "size", in = ParameterIn.QUERY)
    @Parameter(name = "sort", in = ParameterIn.QUERY)
    @SecurityRequirements
    @GetMapping("/list")
    public Page<CarListItemDto> getCars(@Parameter(hidden = true) Pageable pageable,
                                        @ModelAttribute CarFilterDto carFilterDto) {
        return carService.getCarList(pageable, carFilterDto);
    }


    @GetMapping("/{id}")
    @SecurityRequirements
    public CarDetailsDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }


//    @SecurityRequirements
//    @PostMapping("/create")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CarDetailsDto createCar(@RequestBody CreateCarRequestDto createCarRequestDto) {
//        return carService.createCar(createCarRequestDto);
//    }


//    @SecurityRequirements
//    @PutMapping("/{id}")
//    public CarDetailsDto updateCar(@PathVariable Long id, @RequestBody UpdateCarRequestDto updateCarRequestDto) {
//        return carService.updateCar(id, updateCarRequestDto);
//    }
//
//
//
//    @SecurityRequirements
//    @DeleteMapping("/{id}")
////    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCar(@PathVariable Long id) {
//        carService.deleteCar(id);
//    }
}
