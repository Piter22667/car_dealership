package org.example.car_dealership.util;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.CarImage;
import org.example.car_dealership.model.config.car.*;
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
        return switch (dirName) {
            case "car1" -> createToyota();
            case "car2" -> createHondaAccord();
            case "car3" -> createTeslaModel3();
            case "car4" -> createTeslaCybertruck();
            case "car5" -> createTeslaModelS();
            case "car6" -> createNissanSkyline();
            case "car7" -> createPorsche911();
            case "car8" -> createBMWM3();
            case "car9" -> createAudiR8();
            case "car10" -> createFordMustang();
            case "car11" -> createChevroletCorvette();
            case "car12" -> createLamborghiniHuracan();
            case "car13" -> createFerrari488();
            case "car14" -> createMcLaren720S();
            case "car15" -> createMercedesAMG();
            case "car16" -> createJaguarFType();
            case "car17" -> createMaseratiGranTurismo();
            case "car18" -> createBentleyContinental();
            case "car19" -> createAstonMartinDB11();
            case "car20" -> createRollsRoycePhantom();
            case "car21" -> createBugattiChiron();
            case "car22" -> createKoenigseggAgera();
            case "car23" -> createPaganiHuayra();
            default -> throw new IllegalArgumentException("Invalid car directory: " + dirName);
        };
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

    private Car createTeslaCybertruck() {
        return Car.builder()
                .brand("Tesla")
                .type(Type.PICKUP)
                .model("Cybertruck")
                .year(2024)
                .price(BigDecimal.valueOf(60000))
                .mileage(0)
                .fuelType(FuelType.ELECTRIC)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(0))
                .enginePower(800)
                .doors(4)
                .seats(6)
                .trunkCapacity(2000)
                .fuelConsumption(BigDecimal.valueOf(0))
                .registrationNumber("CY7777TR")
                .manufacturer("Tesla")
                .color("Stainless Steel")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createTeslaModelS() {
        return Car.builder()
                .brand("Tesla")
                .type(Type.SEDAN)
                .model("Model S")
                .year(2024)
                .price(BigDecimal.valueOf(75000))
                .mileage(5000)
                .fuelType(FuelType.ELECTRIC)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(0))
                .enginePower(670)
                .doors(4)
                .seats(5)
                .trunkCapacity(28)
                .fuelConsumption(BigDecimal.valueOf(0))
                .registrationNumber("TS2024MS")
                .manufacturer("Tesla")
                .color("Red Multi-Coat")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createNissanSkyline() {
        return Car.builder()
                .brand("Nissan")
                .type(Type.SUV)
                .model("Skyline GT-R")
                .year(2023)
                .price(BigDecimal.valueOf(110000))
                .mileage(1000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.8))
                .enginePower(565)
                .doors(2)
                .seats(4)
                .trunkCapacity(315)
                .fuelConsumption(BigDecimal.valueOf(11.8))
                .registrationNumber("GT2023R")
                .manufacturer("Nissan")
                .color("Gun Metallic")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createPorsche911() {
        return Car.builder()
                .brand("Porsche")
                .type(Type.CROSSOVER)
                .model("911 Carrera")
                .year(2024)
                .price(BigDecimal.valueOf(120000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.0))
                .enginePower(379)
                .doors(2)
                .seats(4)
                .trunkCapacity(132)
                .fuelConsumption(BigDecimal.valueOf(9.4))
                .registrationNumber("PC91124")
                .manufacturer("Porsche")
                .color("Guards Red")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createBMWM3() {
        return Car.builder()
                .brand("BMW")
                .type(Type.SEDAN)
                .model("M3")
                .year(2024)
                .price(BigDecimal.valueOf(85000))
                .mileage(2000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.0))
                .enginePower(473)
                .doors(4)
                .seats(5)
                .trunkCapacity(480)
                .fuelConsumption(BigDecimal.valueOf(10.2))
                .registrationNumber("BMM324")
                .manufacturer("BMW")
                .color("Alpine White")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createAudiR8() {
        return Car.builder()
                .brand("Audi")
                .type(Type.SEDAN)
                .model("R8 V10")
                .year(2023)
                .price(BigDecimal.valueOf(180000))
                .mileage(500)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(5.2))
                .enginePower(562)
                .doors(2)
                .seats(2)
                .trunkCapacity(112)
                .fuelConsumption(BigDecimal.valueOf(14.9))
                .registrationNumber("AUDI823")
                .manufacturer("Audi")
                .color("Ibis White")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createFordMustang() {
        return Car.builder()
                .brand("Ford")
                .type(Type.WAGON)
                .model("Mustang GT")
                .year(2024)
                .price(BigDecimal.valueOf(55000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.MANUAL)
                .engineVolume(BigDecimal.valueOf(5.0))
                .enginePower(450)
                .doors(2)
                .seats(4)
                .trunkCapacity(384)
                .fuelConsumption(BigDecimal.valueOf(13.5))
                .registrationNumber("MUST24GT")
                .manufacturer("Ford")
                .color("Race Red")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createChevroletCorvette() {
        return Car.builder()
                .brand("Chevrolet")
                .type(Type.SEDAN)
                .model("Corvette C8")
                .year(2024)
                .price(BigDecimal.valueOf(95000))
                .mileage(1000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(6.2))
                .enginePower(490)
                .doors(2)
                .seats(2)
                .trunkCapacity(356)
                .fuelConsumption(BigDecimal.valueOf(15.6))
                .registrationNumber("CVT24C8")
                .manufacturer("Chevrolet")
                .color("Elkhart Lake Blue Metallic")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createLamborghiniHuracan() {
        return Car.builder()
                .brand("Lamborghini")
                .type(Type.SEDAN)
                .model("Huracán EVO")
                .year(2023)
                .price(BigDecimal.valueOf(320000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(5.2))
                .enginePower(630)
                .doors(2)
                .seats(2)
                .trunkCapacity(100)
                .fuelConsumption(BigDecimal.valueOf(16.8))
                .registrationNumber("LAMBO23")
                .manufacturer("Lamborghini")
                .color("Verde Mantis")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createFerrari488() {
        return Car.builder()
                .brand("Ferrari")
                .type(Type.SEDAN)
                .model("488 GTB")
                .year(2022)
                .price(BigDecimal.valueOf(380000))
                .mileage(2000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.9))
                .enginePower(661)
                .doors(2)
                .seats(2)
                .trunkCapacity(200)
                .fuelConsumption(BigDecimal.valueOf(15.2))
                .registrationNumber("FERR488")
                .manufacturer("Ferrari")
                .color("Rosso Corsa")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createMcLaren720S() {
        return Car.builder()
                .brand("McLaren")
                .type(Type.SEDAN)
                .model("720S")
                .year(2023)
                .price(BigDecimal.valueOf(350000))
                .mileage(500)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(4.0))
                .enginePower(710)
                .doors(2)
                .seats(2)
                .trunkCapacity(150)
                .fuelConsumption(BigDecimal.valueOf(18.7))
                .registrationNumber("MCL720S")
                .manufacturer("McLaren")
                .color("McLaren Orange")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createMercedesAMG() {
        return Car.builder()
                .brand("Mercedes-Benz")
                .type(Type.SEDAN)
                .model("AMG GT 63 S")
                .year(2024)
                .price(BigDecimal.valueOf(160000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(4.0))
                .enginePower(630)
                .doors(4)
                .seats(5)
                .trunkCapacity(420)
                .fuelConsumption(BigDecimal.valueOf(12.3))
                .registrationNumber("MBAMG24")
                .manufacturer("Mercedes-Benz")
                .color("Designo Diamond White")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createJaguarFType() {
        return Car.builder()
                .brand("Jaguar")
                .type(Type.SEDAN)
                .model("F-Type R")
                .year(2023)
                .price(BigDecimal.valueOf(105000))
                .mileage(3000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(5.0))
                .enginePower(575)
                .doors(2)
                .seats(4)
                .trunkCapacity(320)
                .fuelConsumption(BigDecimal.valueOf(14.5))
                .registrationNumber("JAGF23")
                .manufacturer("Jaguar")
                .color("British Racing Green")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createMaseratiGranTurismo() {
        return Car.builder()
                .brand("Maserati")
                .type(Type.SEDAN)
                .model("GranTurismo")
                .year(2024)
                .price(BigDecimal.valueOf(180000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(3.0))
                .enginePower(490)
                .doors(2)
                .seats(4)
                .trunkCapacity(310)
                .fuelConsumption(BigDecimal.valueOf(11.2))
                .registrationNumber("MASGT24")
                .manufacturer("Maserati")
                .color("Blu Emozione")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createBentleyContinental() {
        return Car.builder()
                .brand("Bentley")
                .type(Type.SEDAN)
                .model("Continental GT")
                .year(2023)
                .price(BigDecimal.valueOf(240000))
                .mileage(1000)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(6.0))
                .enginePower(626)
                .doors(2)
                .seats(4)
                .trunkCapacity(358)
                .fuelConsumption(BigDecimal.valueOf(15.8))
                .registrationNumber("BENT23")
                .manufacturer("Bentley")
                .color("Dragon Red")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createAstonMartinDB11() {
        return Car.builder()
                .brand("Aston Martin")
                .type(Type.SEDAN)
                .model("DB11")
                .year(2024)
                .price(BigDecimal.valueOf(220000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(5.2))
                .enginePower(503)
                .doors(2)
                .seats(4)
                .trunkCapacity(270)
                .fuelConsumption(BigDecimal.valueOf(14.3))
                .registrationNumber("AMDB1124")
                .manufacturer("Aston Martin")
                .color("Stratus White")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createRollsRoycePhantom() {
        return Car.builder()
                .brand("Rolls-Royce")
                .type(Type.MINIVAN)
                .model("Phantom")
                .year(2023)
                .price(BigDecimal.valueOf(450000))
                .mileage(500)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(6.75))
                .enginePower(563)
                .doors(4)
                .seats(5)
                .trunkCapacity(450)
                .fuelConsumption(BigDecimal.valueOf(14.7))
                .registrationNumber("RRPH23")
                .manufacturer("Rolls-Royce")
                .color("Midnight Sapphire")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createBugattiChiron() {
        return Car.builder()
                .brand("Bugatti")
                .type(Type.SEDAN)
                .model("Chiron")
                .year(2022)
                .price(BigDecimal.valueOf(3500000))
                .mileage(0)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(8.0))
                .enginePower(1479)
                .doors(2)
                .seats(2)
                .trunkCapacity(44)
                .fuelConsumption(BigDecimal.valueOf(22.5))
                .registrationNumber("BUGCH22")
                .manufacturer("Bugatti")
                .color("Nocturne Black")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createKoenigseggAgera() {
        return Car.builder()
                .brand("Koenigsegg")
                .type(Type.SEDAN)
                .model("Agera RS")
                .year(2021)
                .price(BigDecimal.valueOf(2800000))
                .mileage(100)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(5.0))
                .enginePower(1160)
                .doors(2)
                .seats(2)
                .trunkCapacity(120)
                .fuelConsumption(BigDecimal.valueOf(18.9))
                .registrationNumber("KOEN21")
                .manufacturer("Koenigsegg")
                .color("Clear Carbon")
                .interior(Interior.LEATHER)
                .build();
    }

    private Car createPaganiHuayra() {
        return Car.builder()
                .brand("Pagani")
                .type(Type.SEDAN)
                .model("Huayra BC")
                .year(2020)
                .price(BigDecimal.valueOf(3100000))
                .mileage(200)
                .fuelType(FuelType.PETROL)
                .transmission(Transmission.AUTOMATIC)
                .engineVolume(BigDecimal.valueOf(6.0))
                .enginePower(791)
                .doors(2)
                .seats(2)
                .trunkCapacity(80)
                .fuelConsumption(BigDecimal.valueOf(17.3))
                .registrationNumber("PAG20")
                .manufacturer("Pagani")
                .color("Viola Tempesta")
                .interior(Interior.LEATHER)
                .build();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "jpg";
    }

}
