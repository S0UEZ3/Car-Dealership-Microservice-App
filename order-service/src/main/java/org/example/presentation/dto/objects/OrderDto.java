package org.example.presentation.dto.objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.dataAccess.models.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "orderType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderInStockDto.class, name = "IN_STOCK"),
        @JsonSubTypes.Type(value = OrderCustomDto.class, name = "CUSTOM")
})
@Schema(description = "Base order data transfer object")
@EqualsAndHashCode(callSuper = true)
public abstract class OrderDto extends BaseDto {
    @Schema(description = "Client who made the order")
    private UserDto client;

    @Schema(description = "Manager assigned to the order")
    private UserDto manager;
}