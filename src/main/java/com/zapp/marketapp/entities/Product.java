package com.zapp.marketapp.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "user_id")
    private Integer userId;


    @Column(name = "category_id")
    private Integer categoryId;


    private String name;

    private String description;

    @JsonBackReference(value = "user-product")
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne
    @JsonBackReference(value = "category-product")
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @JsonBackReference
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Column(name = "in_stock")
    private Integer inStock = 0;

    private double price;

    private String color = "Multicolor";
    private Boolean state = true;
    private String img;
    private LocalDateTime creation_date = LocalDateTime.now();
    private LocalDateTime update_date = LocalDateTime.now();
}
