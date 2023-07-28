package com.zapp.marketapp.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JsonBackReference("user-categories")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;


    @JsonIgnoreProperties({"categoryId"})
    @JsonManagedReference(value = "category-product")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    private String name;
    private String img;
    private Boolean state;
}
