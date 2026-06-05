/*
package org.example.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcConfig {

    @Value("${grpc.client.storage-service.address:localhost:9090}")
    private String storageServiceAddress;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forTarget(storageServiceAddress)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .build();
    }
}
*/
