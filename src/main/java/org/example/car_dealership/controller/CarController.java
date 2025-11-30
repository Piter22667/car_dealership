package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.example.car_dealership.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CarDetailsDto createCar(
            @Valid @ModelAttribute CreateCarRequestDto createCarRequestDto,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("originImages") List<MultipartFile> originImages) {
        return carService.createCar(createCarRequestDto, thumbnail, originImages);
    }



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
