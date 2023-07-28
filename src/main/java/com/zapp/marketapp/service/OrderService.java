package com.zapp.marketapp.service;

import com.zapp.marketapp.dto.OrderDTO;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.OrderMapper;
import com.zapp.marketapp.entities.Order;
import com.zapp.marketapp.entities.Product;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.helpers.CurrentAuthUser;
import com.zapp.marketapp.repository.JpaOrder;
import com.zapp.marketapp.repository.JpaProduct;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.OrderId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService  {

    private final JpaOrder jpaOrder;
    private final JpaProduct jpaProduct;
    private final JpaUser jpaUser;
    private final OrderMapper orderMapper;
    private final CurrentAuthUser currentAuthUser;
    public OrderDTO createOrder(OrderDTO orderDTO)  {
        UserDTO authUser = currentAuthUser.getUserAuthenticated();
        OrderId orderId = orderDTO.getOrderId();
        orderId.setUserId(authUser.getUserId());

        orderDTO.setOrderId(orderId);

        Integer userId = orderDTO.getOrderId().getUserId();
        Integer productId = orderDTO.getOrderId().getProductId();

        jpaOrder.findById(orderDTO.getOrderId())
                        .ifPresent(order  -> {
                            throw new EntityException(
                                    false,
                                    "Este pedido ya está registrado",
                                    "orderId",
                                    400,
                                    "Order"
                            );
                        });

       Product productFound = jpaProduct.findByProductIdAndState(productId,true)
                .map(product -> {
                    if(orderDTO.getQuantity() > product.getInStock()) {
                        throw new EntityException(
                                false,
                                "La cantidad excede al del producto",
                                "quantity",
                                400,
                                "Order"
                        );
                    }
                    return product;
                })
                .orElseThrow(
                        () -> new EntityException(
                                false,
                                "Producto no encontrado con ese ID",
                                "orderId",
                                404,
                                "Order"
                        )
                );

        jpaUser.findByUserIdAndState(userId,true)
                .orElseThrow(
                        () -> new EntityException(
                                false,
                                "Usuario no encontrado con ese ID",
                                "orderId.userId",
                                404,
                                "Order"
                        )
                );

        Order order = orderMapper.toOrder(orderDTO);
        order.setProduct(productFound);
        return orderMapper.toOrderDTO(jpaOrder.save(order));
    }

    public OrderDTO updateOrder(OrderDTO order) {
        UserDTO authUser = currentAuthUser.getUserAuthenticated();
        OrderId orderId = order.getOrderId();
        orderId.setUserId(authUser.getUserId());
        order.setOrderId(orderId);

        System.out.println(orderId);
      return jpaOrder.findById(order.getOrderId()).map(orderFound -> {

          OrderDTO orderDTO = orderMapper.toOrderDTO(orderFound);

          if(order.getQuantity() > orderDTO.getProduct().getInStock()){
              throw new EntityException(
                      false,
                      "La cantidad excede al del producto",
                      "quantity",
                      400,
                      "Order"
              );
          }
          orderDTO.setQuantity(order.getQuantity());
          orderDTO.setUpdatedAt(LocalDateTime.now());

          Order toOrder = orderMapper.toOrder(orderDTO);
          return orderMapper.toOrderDTO(jpaOrder.save(toOrder));

      }).orElseThrow(
              () -> new EntityException(
                      false,
                      "Pedido no encontrado con ese ID",
                      "orderId",
                      404,
                      "Order"
              )
      );

    }


    public Map<String ,Object> deleteOrder(OrderId orderId) {
        UserDTO authUser = currentAuthUser.getUserAuthenticated();
        orderId.setUserId(authUser.getUserId());

        return jpaOrder.findById(orderId).map(order -> {


            jpaOrder.deleteById(orderId);

            Map<String, Object> resp = new HashMap<>();
            resp.put("ok",true);
            resp.put("msg", "Pedido eliminado satisfactoriamente");

            return resp;
        }).orElseThrow(
                () -> new EntityException(
                        false,
                        "Pedido no encontrado con ese ID",
                        "path_variable",
                        404,
                        "Order"
                )
        );
    }

    public List<OrderDTO> orders() {
        System.out.println(jpaOrder.total());
        return orderMapper.toOrdersDTO(jpaOrder.findAll());
    }

    public List<OrderDTO> getOrdersByUserId(Integer id) {
        List<Order> orders = jpaOrder.findAllByOrderIdUserId(id);
        return orderMapper.toOrdersDTO(orders);
    }


}
