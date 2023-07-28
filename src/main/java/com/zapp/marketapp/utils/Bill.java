package com.zapp.marketapp.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zapp.marketapp.dto.OrderDTO;
import com.zapp.marketapp.entities.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Bill implements Serializable {

    @Getter
    @Setter
    private List<OrderDTO> orders = new ArrayList<>();

    @JsonInclude
    @Setter
    @Transient
    private double total;

    public double getTotal() {
        this.total = this.getOrders().stream()
                .map(OrderDTO::getSubtotal)
                .reduce(0.0,(accum,subtotal) -> accum + subtotal);
        return total;
    }

}
