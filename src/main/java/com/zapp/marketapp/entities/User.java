package com.zapp.marketapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zapp.marketapp.utils.Roles;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.CLIENT_ROLE;


    @JsonManagedReference(value = "user-product")
    //@JsonIgnoreProperties({"userId","orders"})
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products_created = new ArrayList<>();

    @JsonManagedReference(value = "user-order")
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    //@JsonIgnoreProperties({"products", "user_id", "img"})
    @JsonManagedReference("user-categories")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Category> categories_created = new ArrayList<>();

    private String img;
    //private String address;
    private boolean google;
    private boolean state = true;


}