package org.example.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.application.services.CarStockService;
import org.example.dataAccess.models.Car;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CarStockGrpcService extends CarStockServiceGrpc.CarStockServiceImplBase {

    private final CarStockService carStockService;

    public void getAllCars(EmptyRequest request, StreamObserver<CarsResponse> responseObserver) {
        log.info("gRPC request: getAllCars");
        try {
            var cars = carStockService.findAllInStock();
            var response = CarsResponse.newBuilder()
                    .addAllCars(cars.stream().map(this::toProto).toList())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error in getAllCars", e);
            responseObserver.onError(e);
        }
    }

    public void getCarById(CarIdRequest request, StreamObserver<CarResponse> responseObserver) {
        log.info("gRPC request: getCarById, id={}", request.getId());
        try {
            var car = carStockService.findById(java.util.UUID.fromString(request.getId()));
            var response = CarResponse.newBuilder().setCar(toProto(car)).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error in getCarById", e);
            responseObserver.onError(e);
        }
    }

    private org.example.grpc.Car toProto(org.example.dataAccess.models.Car entity) {
        return org.example.grpc.Car.newBuilder()
                .setId(entity.getId().toString())
                .setModelId(entity.getModelId().toString())
                .setBodyType(entity.getBodyType() != null ? entity.getBodyType().name() : "")
                .setFuelType(entity.getFuelType() != null ? entity.getFuelType().name() : "")
                .setEnginePower(entity.getEnginePower())
                .setEngineVolume(entity.getEngineVolume())
                .setTransmission(entity.getTransmission() != null ? entity.getTransmission().name() : "")
                .setDriveType(entity.getDriveType() != null ? entity.getDriveType().name() : "")
                .setColor(entity.getColor() != null ? entity.getColor() : "")
                .setInStock(entity.isInStock())
                .setTotalCost(entity.getTotalCost())
                .setAvailableForTestDrive(entity.isAvailableForTestDrive())
                .build();
    }
}