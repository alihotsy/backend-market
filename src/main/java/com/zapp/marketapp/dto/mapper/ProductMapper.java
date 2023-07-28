package com.zapp.marketapp.dto.mapper;

import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.entities.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(source = "productId", target = "productId"),
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "categoryId", target = "categoryId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "inStock", target = "inStock"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "state", target = "state"),
            @Mapping(source = "img", target = "image"),
            @Mapping(source = "creation_date", target = "createdAt"),
            @Mapping(source = "update_date", target = "updatedAt")
    })
    ProductDTO toProductDTO(Product product);
    List<ProductDTO> toProductsDTO(List<Product> products);

    @InheritInverseConfiguration
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductDTO productDTO);
}
