package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.example.car_dealership.dto.UpdateCarRequestDto;
import org.example.car_dealership.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarController implements CarControllerInterface {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Parameter(name="page", in=ParameterIn.QUERY)
    @Parameter(name="size", in= ParameterIn.QUERY)
    @Parameter(name="sort", in= ParameterIn.QUERY)

//    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirements
    @GetMapping("/list")
    public Page<CarListItemDto> getCars(@Parameter(hidden = true) Pageable pageable) {
        return carService.getCarList(pageable);
    }


    @GetMapping("/{id}")
    @SecurityRequirements
    public CarDetailsDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }



    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirements
    public CarDetailsDto createCar(@RequestBody CreateCarRequestDto createCarRequestDto) {
        return carService.createCar(createCarRequestDto);
    }



    @SecurityRequirements
    @PutMapping("/{id}")
    public CarDetailsDto updateCar(@PathVariable Long id, @RequestBody UpdateCarRequestDto updateCarRequestDto) {
        return carService.updateCar(id, updateCarRequestDto);
    }



    @SecurityRequirements
    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
