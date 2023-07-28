package com.zapp.marketapp.utils;

import com.zapp.marketapp.dto.ProductDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsPageable {

    private int page;
    private int size;
    private boolean isEmpty;
    private long total;
    private List<ProductDTO> products = new ArrayList<>();
}
