package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Component data transfer object")
@EqualsAndHashCode(callSuper = true)
public class ComponentDto extends BaseDto {
    @Schema(description = "Component type", required = true)
    private ComponentType type;

    @Schema(description = "Component name", example = "Leather steering wheel")
    private String name;

    @Schema(description = "Component description")
    private String description;

    @Schema(description = "Component price")
    private double price;

    @Schema(description = "Compatible car models")
    private List<CarModelDto> compatibleModels;
}
