package com.zapp.marketapp.controller;

import com.zapp.marketapp.dto.CategoryDTO;
import com.zapp.marketapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<CategoryDTO>> categories() {
        return ResponseEntity.ok(categoryService.categories());
    }

    //TODO: Revisar si es solo para ADMIN_ROLE
    @GetMapping("/id/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("categoryId") Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Secured("ADMIN_ROLE")
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @Secured("ADMIN_ROLE")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable("categoryId") Integer id,
            @RequestBody @Valid CategoryDTO categoryDTO
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id,categoryDTO));
    }

    @Secured("ADMIN_ROLE")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String,Object>> deleteCategory(
            @PathVariable("categoryId") Integer id
    ){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
