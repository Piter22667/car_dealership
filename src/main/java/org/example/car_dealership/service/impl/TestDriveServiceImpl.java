package org.example.car_dealership.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.car_dealership.dto.TestDriveForUserDto;
import org.example.car_dealership.dto.TestDriveResponseDto;
import org.example.car_dealership.exception.*;
import org.example.car_dealership.mapper.TestDriveMapper;
import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.TestDrive;
import org.example.car_dealership.model.TestDriveStatusHistory;
import org.example.car_dealership.model.User;
import org.example.car_dealership.model.config.car.CarStatus;
import org.example.car_dealership.model.config.testDrive.TestDriveStatus;
import org.example.car_dealership.repository.CarRepository;
import org.example.car_dealership.repository.TestDriveRepository;
import org.example.car_dealership.repository.TestDriveStatusHistoryRepository;
import org.example.car_dealership.repository.UserRepository;
import org.example.car_dealership.service.TestDriveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TestDriveServiceImpl implements TestDriveService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final TestDriveRepository testDriveRepository;
    private final TestDriveStatusHistoryRepository testDriveStatusHistoryRepository;
    private final TestDriveMapper testDriveMapper;

    public TestDriveServiceImpl(UserRepository userRepository, CarRepository carRepository, TestDriveRepository testDriveRepository, TestDriveStatusHistoryRepository testDriveStatusHistoryRepository, TestDriveMapper testDriveMapper) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.testDriveRepository = testDriveRepository;
        this.testDriveStatusHistoryRepository = testDriveStatusHistoryRepository;
        this.testDriveMapper = testDriveMapper;
    }

    @Override
    @Transactional
    public TestDriveResponseDto createTestDrive(String clientEmail, Long carId, LocalDateTime scheduledAt) {


        User user = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new UserWithGivenEmailForLoginNotFoundException("User not found with given email."));

        Car car = carRepository.findByIdWithLock(carId)
                .orElseThrow(() -> new CarNotExistException("Car not found with given id."));


        if (testDriveRepository.existsByUserIdAndCarIdAndCurrentStatusIn(
                user.getId(),
                car.getId(),
                List.of(TestDriveStatus.SCHEDULED))) {
            throw new TestDriveAlreadyExistsException("An active test drive already exists for this car by current user.");
        }

        if (car.getStatus() == CarStatus.RESERVED_FOR_TEST_DRIVE) {
            throw new CarNotAvailableException("This car is already reserved for test drive by another user.");
        }

        // Перевірка: чи автомобіль доступний взагалі
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new CarNotAvailableException("This car is not available for test drive. Current status: " + car.getStatus());
        }

        testDriveRepository.findTopByUserIdAndCurrentStatusInOrderByScheduledAtDesc(
                        user.getId(),
                        List.of(TestDriveStatus.SCHEDULED, TestDriveStatus.COMPLETED))
                .ifPresent(lastTestDrive -> {

                    LocalDateTime earliestAllowedTime;
                    if (lastTestDrive.getCurrentStatus() == TestDriveStatus.COMPLETED) {
                        earliestAllowedTime = lastTestDrive.getLastChangedStatusAt().plusHours(24);
                    } else {
                        earliestAllowedTime = lastTestDrive.getScheduledAt().plusHours(24);
                    }

                    if (scheduledAt.isBefore(earliestAllowedTime)) {
                        throw new TestDriveScheduleViolationException(
                                "You can schedule next test drive only 24 hours after the previous one. Earliest allowed time: " + earliestAllowedTime);
                    }
                });


        // Змінюємо статус автомобіля на RESERVED_FOR_TEST_DRIVE
        car.setStatus(CarStatus.RESERVED_FOR_TEST_DRIVE);
        carRepository.save(car);
        log.info("Car status changed to RESERVED_FOR_TEST_DRIVE for car: {}", carId);


        TestDrive testDrive = new TestDrive();
        testDrive.setUser(user);
        testDrive.setCar(car);
        testDrive.setScheduledAt(scheduledAt);
        testDrive.setCurrentStatus(TestDriveStatus.SCHEDULED);
        testDrive.setLastChangedStatusAt(LocalDateTime.now());

        testDrive = testDriveRepository.save(testDrive);

        TestDriveStatusHistory testDriveStatusHistory = new TestDriveStatusHistory();
        testDriveStatusHistory.setTestDrive(testDrive);
        testDriveStatusHistory.setUserWhoChangedTestDriveStatus(user);
        testDriveStatusHistory.setStatus(TestDriveStatus.SCHEDULED);
        testDriveStatusHistory.setChangedAt(LocalDateTime.now());

        testDriveStatusHistoryRepository.save(testDriveStatusHistory);

        log.info("Test drive created successfully for car : {} by user: {}", carId, clientEmail);

        return testDriveMapper.toTestDriveResponseDto(testDrive);
    }

    @Override
    public List<TestDriveForUserDto> getTestDrivesForUser(String clientEmail) {
        User user = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new UserWithGivenEmailForLoginNotFoundException("User not found with given email."));

        List<TestDrive> testDrives = testDriveRepository.findByUserId(user.getId());

        if (testDrives == null) {
            throw new TestDriveNotFoundException("Test drives not found for user: " + clientEmail);
        }

        return testDriveMapper.toTestDriveForUserDtoList(testDrives);
    }
}
