package org.example.presentation.dto.mapper;

import org.example.dataAccess.models.Order;
import org.example.dataAccess.models.OrderCustom;
import org.example.dataAccess.models.OrderInStock;
import org.example.presentation.dto.objects.OrderCustomDto;
import org.example.presentation.dto.objects.OrderDto;
import org.example.presentation.dto.objects.OrderInStockDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, CarModelMapper.class, ComponentMapper.class})
public interface OrderMapper {

    default OrderDto toDto(Order order) {
        if (order instanceof OrderInStock inStock) {
            return toInStockDto(inStock);
        } else if (order instanceof OrderCustom custom) {
            return toCustomDto(custom);
        }
        throw new IllegalArgumentException("Unknown entity type: " + order.getClass());
    }

    default Order toEntity(OrderDto orderDto) {
        if (orderDto instanceof OrderInStockDto inStockDto) {
            return toInStockEntity(inStockDto);
        } else if (orderDto instanceof OrderCustomDto customDto) {
            return toCustomEntity(customDto);
        }
        throw new IllegalArgumentException("Unknown DTO type: " + orderDto.getClass());
    }

    @Mapping(target = "carId", source = "carId")
    OrderInStockDto toInStockDto(OrderInStock order);

    @Mapping(target = "carId", source = "carId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    OrderInStock toInStockEntity(OrderInStockDto orderDto);

    OrderCustomDto toCustomDto(OrderCustom order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    OrderCustom toCustomEntity(OrderCustomDto orderDto);

    default void updateEntityFromDto(OrderDto dto, @MappingTarget Order entity) {
        switch (dto) {
            case OrderInStockDto inStockDto when entity instanceof OrderInStock inStockEntity ->
                    updateInStockEntityFromDto(inStockDto, inStockEntity);
            case OrderCustomDto customDto when entity instanceof OrderCustom customEntity ->
                    updateCustomEntityFromDto(customDto, customEntity);
            default -> throw new IllegalArgumentException("Unsupported DTO type or entity type mismatch");
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateInStockEntityFromDto(OrderInStockDto dto, @MappingTarget OrderInStock entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateCustomEntityFromDto(OrderCustomDto dto, @MappingTarget OrderCustom entity);

    List<OrderDto> toDtoList(List<Order> orders);
    List<OrderInStockDto> toInStockDtoList(List<OrderInStock> orders);
    List<OrderCustomDto> toCustomDtoList(List<OrderCustom> orders);
}
