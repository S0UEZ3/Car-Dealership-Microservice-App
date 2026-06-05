package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.dataAccess.models.CarBodyType;
import org.example.dataAccess.models.CarDriveType;
import org.example.dataAccess.models.CarFuelType;
import org.example.dataAccess.models.CarTransmissionType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Car model data transfer object")
@EqualsAndHashCode(callSuper = true)
public class CarModelDto extends BaseDto {
    @Schema(description = "Brand name", example = "Toyota")
    private String brand;

    @Schema(description = "Model name", example = "Camry")
    private String model;

    @Schema(description = "Base price")
    private Double basePrice;

    @Schema(description = "Available body types")
    private List<CarBodyType> availableBodyTypes;

    @Schema(description = "Available fuel types")
    private List<CarFuelType> availableFuelTypes;

    @Schema(description = "Available transmissions")
    private List<CarTransmissionType> availableTransmissions;

    @Schema(description = "Available drive types")
    private List<CarDriveType> availableDriveTypes;
}
