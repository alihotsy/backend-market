package com.zapp.marketapp.controller;

import com.zapp.marketapp.dto.OrderDTO;
import com.zapp.marketapp.entities.Order;
import com.zapp.marketapp.service.OrderService;
import com.zapp.marketapp.utils.OrderId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> orders() {
        return ResponseEntity.ok(orderService.orders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OrderDTO> updateOrder (@RequestBody @Valid OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.updateOrder(orderDTO),HttpStatus.OK);
    }

    @DeleteMapping("/orderId/{productId}/{userId}")
    public ResponseEntity<Map<String,Object>> deleteOrder(OrderId orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }


}
