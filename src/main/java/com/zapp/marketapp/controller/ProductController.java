package com.zapp.marketapp.controller;

import com.zapp.marketapp.dto.ProductDTO;
import com.zapp.marketapp.service.ProductService;
import com.zapp.marketapp.utils.ProductsPageable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ProductsPageable> products(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "false") boolean all
    ) {
        return ResponseEntity.ok(productService.products(page,size,all));
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Secured({"ADMIN_ROLE","INVENTORY_ROLE"})
    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    @Secured({"ADMIN_ROLE","INVENTORY_ROLE"})
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("productId") Integer id,
            @RequestBody @Valid ProductDTO newChanges
    ) {
        return new ResponseEntity<>(productService.updateProduct(id,newChanges), HttpStatus.OK);
    }

    @Secured({"ADMIN_ROLE","INVENTORY_ROLE"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }


}
