package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Custom order data transfer object")
public class OrderCustomDto extends OrderDto {
    @Schema(description = "Car model for custom configuration")
    private CarModelDto carModel;

    @Schema(description = "Selected components by type")
    private Map<ComponentType, ComponentDto> selectedComponents;

    @Schema(description = "Order status")
    private OrderStatusCustom status;
}
