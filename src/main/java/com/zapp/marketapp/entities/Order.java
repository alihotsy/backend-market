package com.zapp.marketapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zapp.marketapp.utils.OrderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class Order {

    @EmbeddedId
    private OrderId orderId;

    private Integer quantity;


    @JsonBackReference(value = "user-order")
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private boolean state = true;
    private LocalDateTime creation_date = LocalDateTime.now();
    private LocalDateTime update_date = LocalDateTime.now();

}
