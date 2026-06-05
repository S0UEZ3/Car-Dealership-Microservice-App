package org.example.presentation.dto.objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "In-stock order data transfer object")
public class OrderInStockDto extends OrderDto {

    @Schema(description = "Car ID being ordered")
    private UUID carId;

    @Schema(description = "Order status")
    private OrderStatusInStock status;
}
