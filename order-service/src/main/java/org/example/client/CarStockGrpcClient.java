package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.Car;
import org.example.grpc.CarStockServiceGrpc;
import org.example.grpc.EmptyRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarStockGrpcClient {

    @GrpcClient("storage-service")
    private CarStockServiceGrpc.CarStockServiceBlockingStub carStockServiceStub;

    public List<Car> getAllCars() {
        log.info("Calling gRPC getAllCars");
        var response = carStockServiceStub.getAllCars(EmptyRequest.newBuilder().build());
        return response.getCarsList();
    }

    public Car getCarById(UUID id) {
        log.info("Calling gRPC getCarById, id={}", id);
        var request = org.example.grpc.CarIdRequest.newBuilder()
                .setId(id.toString())
                .build();
        var response = carStockServiceStub.getCarById(request);
        return response.getCar();
    }
}