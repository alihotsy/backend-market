package com.zapp.marketapp.helpers;

import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.CategoryMapper;
import com.zapp.marketapp.dto.mapper.ProductMapper;
import com.zapp.marketapp.dto.mapper.UserMapper;
import com.zapp.marketapp.entities.Product;
import com.zapp.marketapp.entities.User;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.exceptions.FileException;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Controller
public class SaveImage {
    private final JpaCategory jpaCategory;
    private final JpaUser jpaUser;
    private final JpaProduct jpaProduct;

    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;


    public String getImageSaved(String type,String absolutePath,String newImage,Integer id) throws Exception {


        return switch (type) {

            case "users" -> jpaUser.findByUserIdAndState(id, true)
                    .map(user -> {
                        Path userImagePath = Paths.get(absolutePath + "//" + user.getImg());
                        if (Files.exists(userImagePath)) {
                            try {
                                Files.delete(userImagePath);
                            } catch (IOException e) {
                                throw new FileException("Algo salió mal al eliminar la imagen",500);
                            }
                        }
                        user.setImg(newImage);
                        UserDTO userUpdated = userMapper.toUserDTO(jpaUser.save(user));
                        return userUpdated.getImage();
                    }).orElseThrow(
                            () -> new EntityException(
                                    false,
                                    "No existe el usuario con ese ID",
                                    "path_variable",
                                    404,
                                    "User"
                            )
                    );

            case "products" -> jpaProduct.findByProductIdAndState(id, true)
                    .map(product -> {
                        Path userImagePath = Paths.get(absolutePath + "//" + product.getImg());
                        if (Files.exists(userImagePath)) {
                            try {
                                Files.delete(userImagePath);
                            } catch (IOException e) {
                                throw new FileException(e.getMessage(),500);
                            }
                        }
                        product.setImg(newImage);
                        ProductDTO userUpdated = productMapper.toProductDTO(jpaProduct.save(product));
                        return userUpdated.getImage();
                    }).orElseThrow(
                            () -> new EntityException(
                                    false,
                                    "No existe el product con ese ID",
                                    "path_variable",
                                    404,
                                    "Product"
                            )
                    );

            case "categories" -> jpaCategory.findByCategoryIdAndState(id, true)
                    .map(category -> {
                        Path userImagePath = Paths.get(absolutePath + "//" + category.getImg());
                        if (Files.exists(userImagePath)) {
                            try {
                                Files.delete(userImagePath);
                            } catch (IOException e) {
                                throw new FileException("Algo salió mal al eliminar la imagen",500);
                            }
                        }
                        category.setImg(newImage);
                        CategoryDTO categoryDTO = categoryMapper.toCategoryDTO(jpaCategory.save(category));
                        return categoryDTO.getImage();
                    }).orElseThrow(
                            () -> new EntityException(
                                    false,
                                    "No existe la categoría con ese ID",
                                    "path_variable",
                                    404,
                                    "Category"
                            )
                    );

            default -> throw new Exception("Algo salió mal, hable con el ADMIN");
        };
    }

}
