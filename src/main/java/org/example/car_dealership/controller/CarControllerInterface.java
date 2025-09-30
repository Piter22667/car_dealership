package org.example.car_dealership.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.car_dealership.dto.CarDetailsDto;
import org.example.car_dealership.dto.CarListItemDto;
import org.example.car_dealership.dto.CreateCarRequestDto;
import org.example.car_dealership.dto.UpdateCarRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarControllerInterface {


    @Operation(
            summary = "Створити новий автомобіль",
            description = "Буде доступно тільки для адміністраторів",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateCarRequestDto.class),
                            examples = @ExampleObject(
                                    name = "createCar",
                                    value = "{ \"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Petrol\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Автомобіль успішно створено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarDetailsDto.class),
                                    examples = @ExampleObject(
                                            name = "createdCar",
                                            value = "{ \"id\": 1, \"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Petrol\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
                                    )
                            )),
                    @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Доступ заборонено", content = @Content),
            }
    )
    CarDetailsDto createCar(CreateCarRequestDto createCarRequestDto);


    @Operation(summary = "Оновити інформацію про автомобіль за ID",
            description = "Буде доступно тільки для адміністраторів",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCarRequestDto.class),
                            examples = @ExampleObject(
                                    name = "updateCar",
                                    value = "{\"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Petrol\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Автомобіль успішно оновлено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarDetailsDto.class),
                                    examples = @ExampleObject(
                                            name = "updatedCar",
                                            value = "{ \"id\": 1, \"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Petrol\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
                                    )
                            )),
                    @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Доступ заборонено", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Автомобіль не знайдено", content = @Content)
            })
    CarDetailsDto updateCar(Long id, UpdateCarRequestDto updateCarRequestDto);


    @Operation(
            summary = "Видалити автомобіль за ID",
            description = "Буде доступно тільки для адміністраторів",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Автомобіль успішно видалено", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Доступ заборонено", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Автомобіль не знайдено", content = @Content)
            }
    )
    void deleteCar(Long id);


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
                                            value = "{ \"id\": 1, \"brand\": \"Toyota\", \"model\": \"Camry\", \"manufacturer\": \"Toyota Motor Corporation\", \"registrationNumber\": \"AB123CD\", \"engineVolume\": 2.5, \"enginePower\": 203, \"fuelConsumption\": 7.8, \"doors\": 4, \"seats\": 5, \"trunkCapacity\": 427, \"transmission\": \"Automatic\", \"cruiseControl\": true, \"fuelType\": \"Petrol\", \"mileage\": 15000, \"lastServiceDate\": \"2023-05-10\", \"price\": 25000, \"color\": \"White\", \"interior\": \"Leather\", \"type\": \"SEDAN\", \"year\": 2022, \"imageUrls\": [\"https://nextcar.ua/images/companies/1/lucid_gravity/2025-toyota-camry.jpg?1700224269216\", \"https://scout.customerscout.net/Gallery/IMAGES/2025/Toyota/Camry/2025ToyotaCamry-exterior-02.jpg\", \"https://www.auto-data.net/images/f77/Toyota-Camry-IX-XV80.jpg\" ] }"
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
    CarDetailsDto getCarById(Long id);


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
    Page<CarListItemDto> getCars(Pageable pageable);
}
