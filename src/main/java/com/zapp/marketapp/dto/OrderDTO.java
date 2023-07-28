package com.zapp.marketapp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.zapp.marketapp.utils.OrderId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public class OrderDTO {

    @Getter
    @Setter
    @Valid
    @NotNull(message = "El orderId no debe ser vacío")
    private OrderId orderId;

    @Getter
    @Setter
    private ProductDTO product;

    @Getter
    @Setter
    @NotNull(message = "La cantidad no debe ser vacío")
    @Min(value = 1, message = "La cantidad debe ser un entero positivo")
    private Integer quantity;

    @Getter
    @Setter
    private Boolean state = true;

    @Getter
    @Setter
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Setter
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Transient
    @Setter
    @JsonInclude
    private double subtotal;

    public double getSubtotal() {

        this.subtotal = this.quantity * this.product.getPrice();
        return this.subtotal;
    }


}
