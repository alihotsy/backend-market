package com.zapp.marketapp.helpers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.CategoryMapper;
import com.zapp.marketapp.dto.mapper.ProductMapper;
import com.zapp.marketapp.dto.mapper.UserMapper;
import com.zapp.marketapp.entities.User;
import com.zapp.marketapp.exceptions.InvalidTypeException;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class FilterResult {
    private final JpaUser jpaUser;
    private final JpaProduct jpaProduct;
    private final JpaCategory jpaCategory;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    public MappingJacksonValue result(String type,String q) throws Exception {
        SearchFilter searchFilter = new SearchFilter();
        SimpleBeanPropertyFilter filter;
        FilterProvider provider;
        MappingJacksonValue mapping;

        switch (type) {
            case "users" -> {
                List<UserDTO> users = userMapper.toUsersDTO(jpaUser.searchByNameOrEmail(q, q,true));
                searchFilter.setUsers(users);
                searchFilter.setTotal(users.size());
                filter = SimpleBeanPropertyFilter.filterOutAllExcept("users","total");
                provider = new SimpleFilterProvider().addFilter("search_filter", filter);
                mapping = new MappingJacksonValue(searchFilter);
                mapping.setFilters(provider);
                return mapping;
            }
            case "products" -> {
                List<ProductDTO> products = productMapper.toProductsDTO(jpaProduct.findByNameContainingIgnoreCaseAndState(q,true));
                searchFilter.setProducts(products);
                searchFilter.setTotal(products.size());
                filter = SimpleBeanPropertyFilter.filterOutAllExcept("products","total");
                provider = new SimpleFilterProvider().addFilter("search_filter", filter);
                mapping = new MappingJacksonValue(searchFilter);
                mapping.setFilters(provider);
                return mapping;
            }
            case "categories" -> {
                List<CategoryDTO> categories = categoryMapper.toCategoriesDTO(jpaCategory.findByNameContainingIgnoreCaseAndState(q,true));
                searchFilter.setCategories(categories);
                searchFilter.setTotal(categories.size());
                filter = SimpleBeanPropertyFilter.filterOutAllExcept("categories","total");
                provider = new SimpleFilterProvider().addFilter("search_filter", filter);
                mapping = new MappingJacksonValue(searchFilter);
                mapping.setFilters(provider);
                return mapping;
            }
            default -> throw new Exception("Algo salió mal. El tipo no corresponde");
        }
    }
}
