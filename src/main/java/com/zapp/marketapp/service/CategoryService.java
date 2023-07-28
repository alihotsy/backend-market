package com.zapp.marketapp.service;

import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.CategoryMapper;
import com.zapp.marketapp.entities.Category;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.helpers.CurrentAuthUser;
import com.zapp.marketapp.repository.JpaCategory;
import com.zapp.marketapp.repository.JpaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final JpaCategory jpaCategory;
    private final JpaUser jpaUser;

    private final CurrentAuthUser currentAuthUser;
    private final CategoryMapper categoryMapper;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        UserDTO authUser = currentAuthUser.getUserAuthenticated();
        categoryDTO.setUserId(authUser.getUserId());

        jpaUser.findByUserIdAndState(categoryDTO.getUserId(),true)
                .orElseThrow(
                        () -> new EntityException(
                                false,
                                "Usuario no encontrado con ese ID",
                                "userId",
                                404,
                                "Category"
                        )
                );

        jpaCategory.findByNameIgnoreCase(categoryDTO.getName())
                        .ifPresent(categoryFound -> {
                            throw new EntityException(
                                    false,
                                    "El nombre de esta categoría ya existe",
                                    "name",
                                    400,
                                    "Category"
                            );
                        });



        Category category = categoryMapper.toCategory(categoryDTO);
        return categoryMapper.toCategoryDTO(jpaCategory.save(category));
    }

    public CategoryDTO updateCategory(Integer id, CategoryDTO newData){

       Category category = jpaCategory.findByCategoryIdAndState(id,true).orElseThrow(
                () -> new EntityException(
                        false,
                        "Categoría no encontrada con ese ID",
                        "path_variable",
                        404,
                        "Category"
                )
        );
        jpaCategory.findByNameIgnoreCase(newData.getName())
                .ifPresent(categoryFound -> {

                    if(!categoryFound.getCategoryId().equals(id)) {
                        throw new EntityException(
                                false,
                                "Ya existe una categoría con ese nombre",
                                "name",
                                400,
                                "Category"
                        );
                    }

                });
        category.setState(true);
        category.setName(newData.getName());
        return categoryMapper.toCategoryDTO(jpaCategory.save(category));
    }

    public Map<String, Object> deleteCategory(Integer id) {
       return jpaCategory.findByCategoryIdAndState(id,true).map(category -> {
           Map<String,Object> resp = new HashMap<>();
            /*if(!category.getState()) {
                resp.put("ok",true);
                resp.put("msg", "Esta categoría ya fue eliminada!");
                resp.put("previously_deleted", true);
                return resp;
            }*/

            category.setState(false);
            jpaCategory.save(category);
            resp.put("ok",true);
            resp.put("msg", "Categoría eliminado con éxito!");
            return resp;
        }).orElseThrow (
                () -> new EntityException(
                false,
                "Categoría no encontrado con ese ID",
                "path_variable",
                404,
                "Category"
        )
        );
    }

    public CategoryDTO getCategoryById(Integer id){
        return jpaCategory.findByCategoryIdAndState(id,true)
                .map(categoryMapper::toCategoryDTO)
                .orElseThrow(
                        () -> new EntityException(
                                false,
                                "Categoría no encontrado con ese ID",
                                "path_variable",
                                404,
                                "Category"
                        )
                );
    }

    public List<CategoryDTO> categories() {
        return categoryMapper.toCategoriesDTO(jpaCategory.findAll());
    }
}
