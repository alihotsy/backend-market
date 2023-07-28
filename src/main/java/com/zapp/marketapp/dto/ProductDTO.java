package com.zapp.marketapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProductDTO {


    private Integer productId;

    @Min(value = 1, message = "El userId debe ser un número entero positivo")
    private Integer userId;

    @Min(value = 1, message = "El categoryId debe ser un número entero positivo")
    private Integer categoryId;

    @NotBlank(message = "El campo name no debe ser vacío")
    private String name;

    private String description;

    @Min(value = 0, message = "El campo inStock no debe ser un valor negativo")
    private Integer inStock = 0;

    @Min(value = 1, message = "El campo precio debe ser un valor positivo")
    private double price;

    @NotBlank(message = "El campo color no debe ser vacío")
    private String color = "Multicolor";

    @NotNull(message = "El campo state debe ser un valor booleano")
    private Boolean state = true;

    private String image;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


}
