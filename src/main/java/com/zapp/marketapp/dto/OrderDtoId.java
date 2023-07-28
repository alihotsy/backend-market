package com.zapp.marketapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDtoId {
    @NotNull(message = "El productId no debe ser nulo")
    @Min(value = 1, message = "El productId debe ser un entero positivo")
    private Integer productId;

    @NotNull(message = "El userId no debe ser nulo")
    @Min(value = 1, message = "El userId debe ser un entero positivo")
    private Integer userId;
}
