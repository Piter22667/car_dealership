package org.example.car_dealership.util;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.model.config.car.FuelType;
import org.example.car_dealership.model.config.car.ImageVariant;
import org.example.car_dealership.model.config.car.Transmission;
import org.example.car_dealership.model.config.car.Type;
import org.example.car_dealership.repository.CarImageRepository;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.service.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.UUID;

@Component
@Profile("dev")
@Slf4j
public class TestDataLoader implements CommandLineRunner {

    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;
    private final S3Service s3Service;

    public TestDataLoader(CarRepository carRepository, CarImageRepository carImageRepository, S3Service s3Service) {
        this.carRepository = carRepository;
        this.carImageRepository = carImageRepository;
        this.s3Service = s3Service;
    }

    @Override
    public void run(String... args) {
        if (carRepository.count() != 0) {
            log.info("Database already contains test data");
            return;
        }

        log.info("Uploading test data from images... ");

        String baseImagePath = "car-test-images";
        File baseDirectory = new File(baseImagePath);

        if (!baseDirectory.exists()) {
            log.warn("Directory '{}' not found. Skipping test data loading.", baseImagePath);
            return;
        }

        File[] carDirs = baseDirectory.listFiles(File::isDirectory);
        if (carDirs == null || carDirs.length == 0) {
            log.warn("No car directories found in '{}'", baseImagePath);
            return;
        }

        for (File carDir : carDirs) {
            try {
                loadCarWithImage(carDir);
            } catch (Exception e) {
                log.error("Failed to load car from directory: {}", carDir.getName(), e);
            }
        }

        log.info("Test car uploaded and saved successfully");
    }

    private void loadCarWithImage(File carDir) {
        log.info("Processing directory: {}", carDir.getName());

        Car car = createCar(carDir.getName());
        car = carRepository.save(car);

        File thumbImage = new File(carDir, "thumb");
        if (thumbImage.exists()) {
            uploadImages(car, thumbImage, ImageVariant.THUMB, true);
        }

        File originImage = new File(carDir, "original");
        if (originImage.exists()) {
            uploadImages(car, originImage, ImageVariant.ORIGIN, false);
        }
    }

    private void uploadImages(Car car, File directory, ImageVariant variant, boolean isPrimary) {
        File[] imageFiles = directory.listFiles((dir, name) ->
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png)$"));

        if (imageFiles == null || imageFiles.length == 0) {
            log.warn("No images found in directory: {}", directory.getPath());
            return;
        }

        boolean isFirst = true;
        for (File imageFile : imageFiles) {
            try {
                String uuid = UUID.randomUUID().toString();
                String extension = getFileExtension(imageFile.getName());
                String imageVariant = getVariantFolder(variant);
                String key = String.format("cars/%d/%s/%s.%s", car.getId(), imageVariant, uuid, extension);

                MockMultipartFile multipartFile = createMultipartFile(imageFile);
                String storageKey = s3Service.uploadMultipartFile(key, multipartFile);

                CarImage carImage = CarImage.builder()
                        .car(car)
                        .storageKey(storageKey)
                        .imageVariant(variant)
                        .isPrimary(isPrimary && isFirst)
                        .build();

                carImageRepository.save(carImage);
                log.info("Uploaded {} image for car {}: {}", variant, car.getId(), storageKey);

                isFirst = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getVariantFolder(ImageVariant variant) {
        return switch (variant) {
            case THUMB -> "thumb";
            case ORIGIN -> "origin";
            default -> "unknown";
        };
    }

    private MockMultipartFile createMultipartFile(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            String contentType = Files.probeContentType(file.toPath());

            if (contentType == null) {
                contentType = "image/jpeg";
            }

            return new MockMultipartFile(
                    file.getName(),
                    file.getName(),
                    contentType,
                    input);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Car createCar(String dirName) {
        switch (dirName) {
            case "car1":
                return createToyota();
            case "car2":
                return createHondaAccord();
            case "car3":
                return createTeslaModel3();
            default:
                throw new IllegalArgumentException("Invalid car directory: " + dirName);
        }
    }

    private Car createToyota() {
        return Car.builder()
                .brand("Toyota")
                .type(Type.SEDAN)
                .model("Camry")
                .year(2021)
                .price(BigDecimal.valueOf(28000))
                .mileage(15000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.2))
                .enginePower(203)
                .doors(4)
                .seats(5)
                .trunkCapacity(68)
                .fuelConsumption(BigDecimal.valueOf(7.4))
                .registrationNumber("АА1234АА")
                .manufacturer("Toyota")
                .build();
    }

    private Car createHondaAccord() {
        return Car.builder()
                .brand("Honda")
                .type(Type.SEDAN)
                .model("Accord")
                .year(2024)
                .price(BigDecimal.valueOf(35000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(2.99))
                .enginePower(256)
                .doors(5)
                .seats(5)
                .trunkCapacity(78)
                .fuelConsumption(BigDecimal.valueOf(7.4))
                .registrationNumber("AE2324RE")
                .manufacturer("Honda")
                .build();
    }

    private Car createTeslaModel3() {
        return Car.builder()
                .brand("Tesla")
                .type(Type.SEDAN)
                .model("Model 3")
                .year(2024)
                .price(BigDecimal.valueOf(28000))
                .mileage(15000)
                .fuelType(FuelType.ELECTRIC)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(0))
                .enginePower(250)
                .doors(5)
                .seats(5)
                .trunkCapacity(0)
                .fuelConsumption(BigDecimal.valueOf(0))
                .registrationNumber("AD2748YE")
                .manufacturer("Tesla")
                .build();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "jpg";
    }

}
