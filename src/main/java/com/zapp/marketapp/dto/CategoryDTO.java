package com.zapp.marketapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class CategoryDTO {

    @Getter
    private Integer categoryId;

    @Getter
    @Min(value = 1, message = "El userId debe ser un entero positivo")
    @Max(value = 2147483647, message = "Valor fuera de rango para el id del usuario")
    private Integer userId;

    private List<ProductDTO> products = new ArrayList<>();

    public List<ProductDTO> getProducts() {
        return products.stream().filter(ProductDTO::getState).toList();
    }

    @Getter
    @NotBlank(message = "El campo name no debe ser vacío")
    private String name;

    @Getter
    private String image;

    @Getter
    @NotNull(message = "El campo state debe ser un valor booleano")
    private Boolean state = true;


}
