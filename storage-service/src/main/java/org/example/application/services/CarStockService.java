package org.example.application.services;

import lombok.RequiredArgsConstructor;
import org.example.dataAccess.models.Car;
import org.example.dataAccess.repositories.ICarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarStockService {
    private final ICarRepository carRepository;

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public List<Car> findAllInStock() {
        return carRepository.findAll().stream()
                .filter(Car::isInStock)
                .collect(Collectors.toList());
    }

    public Car findById(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found: " + id));
    }

    public boolean isCarInStock(UUID id) {
        return carRepository.findById(id)
                .map(Car::isInStock)
                .orElse(false);
    }

    @Transactional
    public Car addCar(Car car) {
        car.setInStock(true);
        return carRepository.save(car);
    }

    @Transactional
    public void reserveCar(UUID id) {
        Car car = findById(id);
        if (!car.isInStock()) {
            throw new RuntimeException("Car is not in stock: " + id);
        }
        car.setInStock(false);
        carRepository.save(car);
    }

    @Transactional
    public void releaseCar(UUID id) {
        Car car = findById(id);
        car.setInStock(true);
        carRepository.save(car);
    }

    @Transactional
    public Car update(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public void deleteById(UUID id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void setTestDriveAvailability(UUID id, boolean available) {
        Car car = findById(id);
        car.setAvailableForTestDrive(available);
        carRepository.save(car);
    }
}
