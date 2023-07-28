package com.zapp.marketapp.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.entities.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonFilter("search_filter")
public class SearchFilter {

    private Integer total;

    
    private List<UserDTO> users;


    private List<ProductDTO> products;


    private List<CategoryDTO> categories;

    public SearchFilter(){

    }

}
