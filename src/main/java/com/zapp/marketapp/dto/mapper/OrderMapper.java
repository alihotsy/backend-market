package com.zapp.marketapp.dto.mapper;

import com.zapp.marketapp.dto.OrderDTO;
import com.zapp.marketapp.entities.Order;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderMapper {
    @Mappings({
            @Mapping(source = "orderId", target = "orderId"),
            @Mapping(source = "product", target = "product"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "state", target = "state"),
            @Mapping(source = "creation_date", target = "createdAt"),
            @Mapping(source = "update_date", target = "updatedAt"),
            @Mapping(target = "subtotal", ignore = true)
    })
    OrderDTO toOrderDTO(Order order);
    List<OrderDTO> toOrdersDTO(List<Order> orders);
    @InheritInverseConfiguration
    @Mapping(target = "user", ignore = true)
    Order toOrder(OrderDTO orderDTO);
}
