package com.zapp.marketapp.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
public class OrderId implements Serializable {


    @Column(name = "product_id")
    private Integer productId;


    @Column(name = "user_id")
    private Integer userId;
}
