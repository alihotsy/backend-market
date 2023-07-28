package com.zapp.marketapp.service;

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
import com.zapp.marketapp.helpers.FilterResult;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final JpaUser jpaUser;
    private final JpaProduct jpaProduct;
    private final JpaCategory jpaCategory;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;


    private final FilterResult filterResult;
    private final static List<String> types = Arrays.asList("users","categories","products");
    public MappingJacksonValue searchByType(String q, String type) throws Exception {
        if(!types.contains(type)) {
            throw new InvalidTypeException("El tipo es inválido, debe ser: "+types);
        }
        return filterResult.result(type,q);

    }

    public MappingJacksonValue globalResults(String q) {

        List<UserDTO> users = userMapper.toUsersDTO(jpaUser.searchByNameOrEmail(q,q,true));
        List<ProductDTO> products = productMapper.toProductsDTO(jpaProduct.findByNameContainingIgnoreCaseAndState(q,true));
        List<CategoryDTO> categories = categoryMapper.toCategoriesDTO(jpaCategory.findByNameContainingIgnoreCaseAndState(q,true));

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setUsers(users);
        searchFilter.setProducts(products);
        searchFilter.setCategories(categories);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("users","products","categories");
        FilterProvider provider = new SimpleFilterProvider().addFilter("search_filter",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(searchFilter);
        mapping.setFilters(provider);
        return mapping;
    }

}
