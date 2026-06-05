package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.dataAccess.models.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Car data transfer object")
@EqualsAndHashCode(callSuper = true)
public class CarDto extends BaseDto {
    @Schema(description = "Car model", required = true)
    private CarModelDto model;

    @Schema(description = "Body type")
    private CarBodyType bodyType;

    @Schema(description = "Fuel type")
    private CarFuelType fuelType;

    @Schema(description = "Engine power (hp)")
    private Double enginePower;

    @Schema(description = "Engine volume (L)")
    private Double engineVolume;

    @Schema(description = "Transmission type")
    private CarTransmissionType transmission;

    @Schema(description = "Drive type")
    private CarDriveType driveType;

    @Schema(description = "Color")
    private String color;

    @Schema(description = "Total cost")
    private Double totalCost;

    @Schema(description = "In stock flag")
    private boolean inStock;

    @Schema(description = "Available for test drive flag")
    private boolean availableForTestDrive;

    @Schema(description = "Configuration components")
    private List<ComponentDto> configuration;
}
