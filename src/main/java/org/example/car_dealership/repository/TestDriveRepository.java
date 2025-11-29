package org.example.car_dealership.repository;

import org.example.car_dealership.model.TestDrive;
import org.example.car_dealership.model.config.testDrive.TestDriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestDriveRepository extends JpaRepository<TestDrive, Long> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TestDrive t " +
            "WHERE t.car.id = :carId AND t.currentStatus = :status")
    boolean existsByCarIdAndCurrentStatus(@Param("carId") Long carId,
                                          @Param("status") TestDriveStatus status);


    boolean existsByUserIdAndCarId(Long userId, Long carId);

    List<TestDrive> findByUserId(Long id);

    Optional<TestDrive> findTopByUserIdAndCurrentStatusInOrderByScheduledAtDesc(Long userId, List<TestDriveStatus> statuses);
}