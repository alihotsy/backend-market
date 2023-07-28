package com.zapp.marketapp.service;

import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.ProductMapper;
import com.zapp.marketapp.entities.Product;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.helpers.CurrentAuthUser;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.ProductsPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final JpaProduct jpaProduct;
    private final JpaUser jpaUser;
    private final JpaCategory jpaCategory;

    private final CurrentAuthUser currentAuthUser;
    private final ProductMapper productMapper;

    public ProductDTO createProduct(ProductDTO productDTO) {

        UserDTO userDTO = currentAuthUser.getUserAuthenticated();

        productDTO.setUserId(userDTO.getUserId());

        jpaUser.findByUserIdAndState(productDTO.getUserId(),true)
                        .orElseThrow(() -> new EntityException(false,"Usuario no encontrado con ese ID","userId",404,"Product"));

        jpaCategory.findByCategoryIdAndState(productDTO.getCategoryId(),true)
                .orElseThrow(() -> new EntityException(false,"Categoría no encontrada con ese ID","categoryId",404,"Product"));

        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(jpaProduct.save(product));
    }

    public ProductsPageable products(int page, int size, boolean all) {
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page,size);
        List<Product> productsEntity = jpaProduct.findAllByState(true,pageable);
        List<ProductDTO> productsDTO = productMapper.toProductsDTO(productsEntity);

        Page<ProductDTO> productsPageDto = new PageImpl<>(productsDTO);


        return ProductsPageable
                .builder()
                .page(page)
                .size(productsPageDto.getSize())
                .isEmpty(productsPageDto.isEmpty())
                .total(productsPageDto.getTotalElements())
                .products(productsPageDto.getContent())
                .build();
    }

    public ProductDTO getProductById(Integer id) {
        return jpaProduct.findByProductIdAndState(id,true)
                .map(productMapper::toProductDTO)
                .orElseThrow(
                        () -> new EntityException(false,"Producto no encontrado con ese ID","path_variable",404,"Product")
                );
    }

    //Nota: La imagen del producto se actualizará en otro servicio****
    public ProductDTO updateProduct(Integer id, ProductDTO newData) {

        jpaCategory.findByCategoryIdAndState(newData.getCategoryId(),true)
                .orElseThrow(() -> new EntityException(false,"Categoría no encontrada con ese ID","categoryId",404,"Product"));

        return jpaProduct.findByProductIdAndState(id,true).map(product -> {
            ProductDTO productDTO = productMapper.toProductDTO(product);

            productDTO.setCategoryId(newData.getCategoryId());
            productDTO.setName(newData.getName());
            productDTO.setDescription(newData.getDescription());
            productDTO.setInStock(newData.getInStock());
            productDTO.setPrice(newData.getPrice());
            productDTO.setColor(newData.getColor());
            productDTO.setState(true);
            productDTO.setUpdatedAt(LocalDateTime.now());
            Product toProduct = productMapper.toProduct(productDTO);
            return productMapper.toProductDTO(jpaProduct.save(toProduct));
        }).orElseThrow(
                () -> new EntityException(false,"Producto no encontrado con ese ID","path_variable",404,"Product")
        );
    }

    public Map<String,Object> deleteProduct(Integer id) {
        return jpaProduct.findByProductIdAndState(id,true).map(product -> {
            Map<String,Object> resp = new HashMap<>();
            ProductDTO productDTO = productMapper.toProductDTO(product);

            /*if(!productDTO.getState()) {
                resp.put("ok", true);
                resp.put("msg", "Este producto ya fue eliminado!");
                resp.put("previously_deleted",true);
                return resp;
            }*/

            productDTO.setState(false);
            Product toProduct = productMapper.toProduct(productDTO);
            jpaProduct.save(toProduct);

            resp.put("ok",true);
            resp.put("msg", "Producto eliminado satisfactoriamente!");
            resp.put("previously_deleted",false);
            return resp;

        }).orElseThrow(
                () -> new EntityException(false,"Producto no encontrado con ese ID","path variable",404,"Product")
        );
    }
}
