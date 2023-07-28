package com.zapp.marketapp.dto.mapper;

import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.entities.Category;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper {

    @Mappings({
            @Mapping(source = "categoryId", target = "categoryId"),
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "products", target = "products"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "img",target = "image"),
            @Mapping(source = "state", target = "state")
    })
    CategoryDTO toCategoryDTO(Category category);
    List<CategoryDTO> toCategoriesDTO(List<Category> categories);

    @InheritInverseConfiguration
    @Mapping(target = "user", ignore = true)
    Category toCategory(CategoryDTO categoryDTO);
}
