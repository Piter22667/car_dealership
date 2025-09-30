package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

    @Parameter(name="page", in=ParameterIn.QUERY)
    @Parameter(name="size", in= ParameterIn.QUERY)
    @Parameter(name="sort", in= ParameterIn.QUERY)
    @Operation(
            summary = "Отримати список автомобілів з пагінацією",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarListItemDto.class),
                                    examples = @ExampleObject(
                                            name = "carsList",
                                            value = "{ \"content\": [ { \"id\": 1, \"title\": \"Toyota Camry\", \"type\": \"SEDAN\", \"year\": 2022, \"mileage\": 15000, \"price\": 25000, \"imageUrl\": \"https://autoua.net/media/uploads/toyota/2021-toyota-camry-xse-hybrid.jpg\" }, { \"id\": 2, \"title\": \"Volkswagen Golf\", \"type\": \"HATCHBACK\", \"year\": 2021, \"mileage\": 8000, \"price\": 18000, \"imageUrl\": \"https://images.prismic.io/carwow/ZpqUKB5LeNNTxT_z_volkswagen-golf-2024-rhd-front34dynamic1.jpg\" } ], \"totalPages\": 1, \"totalElements\": 2, \"size\": 20, \"number\": 0 }"
                                    )
                            )
                    )
            }
    )
//    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirements
    @GetMapping("/list")
    public Page<CarListItemDto> getCars(@Parameter(hidden = true) Pageable pageable) {
        return carService.getCarList(pageable);
    }

    @Operation(
            summary = "Отримати деталі автомобіля за ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarDetailsDto.class),
                                    examples = @ExampleObject(
                                            name = "carDetails",
                                            value = "{ \"id\": 1, \"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Gasoline\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    @SecurityRequirements
    public CarDetailsDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }
}
