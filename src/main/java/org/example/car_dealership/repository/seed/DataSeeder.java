//package org.example.car_dealership.repository.seed;
//import lombok.RequiredArgsConstructor;
//import org.example.car_dealership.model.*;
//import org.example.car_dealership.model.config.car.FuelType;
//import org.example.car_dealership.model.config.car.Interior;
//import org.example.car_dealership.model.config.car.Transmission;
//import org.example.car_dealership.model.config.car.Type;
//import org.example.car_dealership.model.config.chat.SenderType;
//import org.example.car_dealership.model.config.orders.OrderStatus;
//import org.example.car_dealership.model.config.testDrive.TestDriveStatus;
//import org.example.car_dealership.model.config.user.Role;
//import org.example.car_dealership.repository.*;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final CarRepository carRepository;
//    private final CarImageRepository carImageRepository;
//    private final ServiceHistoryRepository serviceHistoryRepository;
//    private final OrderRepository orderRepository;
//    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
//    private final TestDriveRepository testDriveRepository;
//    private final TestDriveStatusHistoryRepository testDriveStatusHistoryRepository;
//    private final ChatMessageRepository chatMessageRepository;
//
//    @Override
//    public void run(String... args) {
//
//        User client = new User();
//        client.setName("Test User");
//        client.setEmail("test@example.com");
//        client.setPassword("hashed_password");
//        client.setRole(Role.ROLE_CLIENT);
//        client = userRepository.save(client);
//
//        User ai = new User();
//        ai.setName("Open ai");
//        ai.setEmail("openAI@ai.com");
//        ai.setPassword("hashed_password");
//        ai.setRole(Role.ROLE_SYSTEM);
//        ai = userRepository.save(ai);
//
//        User admin = new User();
//        admin.setName("Адмін Адмінович");
//        admin.setEmail("admin@example.com");
//        admin.setPassword("hashed_password");
//        admin.setRole(Role.ROLE_ADMIN);
//        admin = userRepository.save(admin);
//
//        Car car1 = new Car();
//        car1.setBrand("BMW");
//        car1.setModel("X7");
//        car1.setManufacturer("BMW");
//        car1.setRegistrationNumber("AB1234BE");
//        car1.setEngineVolume(new BigDecimal("2.5"));
//        car1.setEnginePower(200);
//        car1.setFuelConsumption(new BigDecimal("7.5"));
//        car1.setDoors(4);
//        car1.setSeats(5);
//        car1.setTrunkCapacity(500);
//        car1.setTransmission(Transmission.AUTOMATIC);
//        car1.setCruiseControl(true);
//        car1.setFuelType(FuelType.PETROL);
//        car1.setMileage(10000);
//        car1.setLastServiceDate(LocalDate.now().minusMonths(2));
//        car1.setPrice(new BigDecimal("32000.00"));
//        car1.setColor("Black");
//        car1.setType(Type.CROSSOVER);
//        car1.setYear(2017);
//        car1.setInterior(Interior.LEATHER);
//        car1 = carRepository.save(car1);
//
//        Car car2 = new Car();
//        car2.setBrand("Tesla");
//        car2.setModel("Model 3");
//        car2.setManufacturer("Tesla");
//        car2.setRegistrationNumber("BB5678CC");
//        car2.setEngineVolume(new BigDecimal("0.0"));
//        car2.setEnginePower(250);
//        car2.setFuelConsumption(new BigDecimal("0.0"));
//        car2.setDoors(4);
//        car2.setSeats(5);
//        car2.setTrunkCapacity(425);
//        car2.setTransmission(Transmission.AUTOMATIC);
//        car2.setCruiseControl(true);
//        car2.setFuelType(FuelType.ELECTRIC);
//        car2.setMileage(5000);
//        car2.setLastServiceDate(LocalDate.now().minusMonths(1));
//        car2.setPrice(new BigDecimal("45000.00"));
//        car2.setColor("White");
//        car2.setInterior(Interior.FABRIC);
//        car2.setType(Type.SEDAN);
//        car2.setYear(2024);
//        car2 = carRepository.save(car2);
//
//        CarImage image1 = new CarImage();
//        image1.setCar(car1);
//        image1.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c6/BMW_G07_IMG_4977.jpg/1200px-BMW_G07_IMG_4977.jpg");
//        carImageRepository.save(image1);
//
//        CarImage image2 = new CarImage();
//        image2.setCar(car2);
//        image2.setImageUrl("https://olmaks.ua/wp-content/uploads/2019/05/vmesto-vydeo-2.jpg");
//        carImageRepository.save(image2);
//
//        ServiceHistory service1 = new ServiceHistory();
//        service1.setCar(car1);
//        service1.setServiceDate(LocalDateTime.now().minusMonths(1));
//        service1.setDescription("Планове ТО");
//        service1.setMileage(new BigDecimal("9500"));
//        service1.setCost(new BigDecimal("200.00"));
//        service1.setServiceCenter("Toyota Center Kyiv");
//        serviceHistoryRepository.save(service1);
//
//        ServiceHistory service2 = new ServiceHistory();
//        service2.setCar(car2);
//        service2.setServiceDate(LocalDateTime.now().minusWeeks(3));
//        service2.setDescription("Перевірка батареї");
//        service2.setMileage(new BigDecimal("4800"));
//        service2.setCost(new BigDecimal("150.00"));
//        service2.setServiceCenter("Tesla Service Kyiv");
//        serviceHistoryRepository.save(service2);
//
//        Order order1 = new Order();
//        order1.setUser(client);
//        order1.setCar(car1);
//        order1.setOrderDate(LocalDateTime.now().minusDays(10));
//        order1.setTotalPrice(new BigDecimal("32000.00"));
//        order1.setRequiresTestDrive(true);
//        order1 = orderRepository.save(order1);
//
//        Order order2 = new Order();
//        order2.setUser(client);
//        order2.setCar(car2);
//        order2.setOrderDate(LocalDateTime.now().minusDays(5));
//        order2.setTotalPrice(new BigDecimal("45000.00"));
//        order2.setRequiresTestDrive(false);
//        order2 = orderRepository.save(order2);
//
//        OrderStatusHistory orderStatus1 = new OrderStatusHistory();
//        orderStatus1.setOrder(order1);
//        orderStatus1.setUserWhoChangedOrderStatus(ai);
//        orderStatus1.setStatus(OrderStatus.PROCESSING);
//        orderStatus1.setChangedAt(LocalDateTime.now().minusDays(9));
//        orderStatusHistoryRepository.save(orderStatus1);
//
//        OrderStatusHistory orderStatus2 = new OrderStatusHistory();
//        orderStatus2.setOrder(order2);
//        orderStatus2.setUserWhoChangedOrderStatus(admin);
//        orderStatus2.setStatus(OrderStatus.COMPLETED);
//        orderStatus2.setChangedAt(LocalDateTime.now().minusDays(4));
//        orderStatusHistoryRepository.save(orderStatus2);
//
//        TestDrive testDrive1 = new TestDrive();
//        testDrive1.setUser(client);
//        testDrive1.setCar(car1);
//        testDrive1.setScheduledAt(LocalDateTime.now().plusDays(2));
//        testDrive1 = testDriveRepository.save(testDrive1);
//
//        TestDrive testDrive2 = new TestDrive();
//        testDrive2.setUser(client);
//        testDrive2.setCar(car2);
//        testDrive2.setScheduledAt(LocalDateTime.now().plusDays(5));
//        testDrive2 = testDriveRepository.save(testDrive2);
//
//        TestDriveStatusHistory testDriveStatus1 = new TestDriveStatusHistory();
//        testDriveStatus1.setTestDrive(testDrive1);
//        testDriveStatus1.setUserWhoChangedTestDriveStatus(ai);
//        testDriveStatus1.setStatus(TestDriveStatus.SCHEDULED);
//        testDriveStatus1.setChangedAt(LocalDateTime.now());
//        testDriveStatusHistoryRepository.save(testDriveStatus1);
//
//        TestDriveStatusHistory testDriveStatus2 = new TestDriveStatusHistory();
//        testDriveStatus2.setTestDrive(testDrive2);
//        testDriveStatus2.setUserWhoChangedTestDriveStatus(admin);
//        testDriveStatus2.setStatus(TestDriveStatus.CANCELED);
//        testDriveStatus2.setChangedAt(LocalDateTime.now().plusDays(1));
//        testDriveStatusHistoryRepository.save(testDriveStatus2);
//
//        ChatMessage chat1 = new ChatMessage();
//        chat1.setUser(client);
//        chat1.setSender(SenderType.USER);
//        chat1.setMessageText("Доброго дня! Хочу записатись на тест-драйв.");
//        chat1.setCreatedAt(LocalDateTime.now());
//        chatMessageRepository.save(chat1);
//
//        ChatMessage chat2 = new ChatMessage();
//        chat2.setUser(ai);
//        chat2.setSender(SenderType.MANAGER);
//        chat2.setMessageText("Вітаю! Ваш тест-драйв підтверджено.");
//        chat2.setCreatedAt(LocalDateTime.now().plusMinutes(5));
//        chatMessageRepository.save(chat2);
//    }
//}