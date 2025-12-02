package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.*;
import org.example.car_dealership.service.CarService;
import org.example.car_dealership.service.TestDriveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
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
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<TestDriveResponseDto> createTestDrive(
            @PathVariable Long carId,
            Authentication authentication,
            @Valid @org.springframework.web.bind.annotation.RequestBody TestDriveRequestDto requestDto) {

        log.info("=== TEST DRIVE REQUEST DEBUG ===");
        log.info("CarId: {}", carId);
        log.info("Received request DTO: {}", requestDto);
        log.info("DTO class: {}", requestDto != null ? requestDto.getClass().getName() : "NULL");
        log.info("Received scheduledAt: {}", requestDto != null ? requestDto.getScheduledAt() : "DTO IS NULL");

        if (requestDto != null && requestDto.getScheduledAt() != null) {
            log.info("ScheduledAt details - Year: {}, Month: {}, Day: {}, Hour: {}, Minute: {}, Second: {}",
                    requestDto.getScheduledAt().getYear(),
                    requestDto.getScheduledAt().getMonthValue(),
                    requestDto.getScheduledAt().getDayOfMonth(),
                    requestDto.getScheduledAt().getHour(),
                    requestDto.getScheduledAt().getMinute(),
                    requestDto.getScheduledAt().getSecond());
        }

        log.info("User: {}, Authorities: {}",
                authentication.getName(),
                authentication.getAuthorities());
        log.info("=== END DEBUG ===");

        TestDriveResponseDto testDrive = testDriveService.createTestDrive(
                authentication.getName(),
                carId,
                requestDto.getScheduledAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(testDrive);
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
