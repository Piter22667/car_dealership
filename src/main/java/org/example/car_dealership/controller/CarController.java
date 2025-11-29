package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarFilterDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.TestDriveResponseDto;
import org.example.car_dealership.service.CarService;
import org.example.car_dealership.service.TestDriveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cars")
public class CarController implements CarControllerInterface {

    private final CarService carService;
    private final TestDriveService testDriveService;

    public CarController(CarService carService, TestDriveService testDriveService) {
        this.carService = carService;
        this.testDriveService = testDriveService;
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

    @PostMapping("/testDrive/{carId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<TestDriveResponseDto> createTestDrive(@PathVariable Long carId, Authentication authentication, @RequestParam LocalDateTime scheduledAt) {
        TestDriveResponseDto testDrive = testDriveService.createTestDrive(authentication.getName(), carId, scheduledAt);
        return ResponseEntity.ok(testDrive);
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
