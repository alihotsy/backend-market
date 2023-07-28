package com.zapp.marketapp.dto.mapper;

import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.entities.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class/*ProductMapper.class,CategoryMapper.class*/})
public interface UserMapper {

    @Mappings({
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "role", target = "role"),
            //@Mapping(source = "products_created", target = "products_created"),
            @Mapping(source = "orders", target = "bill.orders"),
            //@Mapping(source = "categories_created", target = "categories_created"),
            @Mapping(source = "img", target = "image"),
            //@Mapping(source = "address", target = "address"),
            @Mapping(source = "google", target = "google"),
            @Mapping(source = "state", target = "state"),
            @Mapping(target = "bill", ignore = true)
    })
    UserDTO toUserDTO(User user);
    List<UserDTO> toUsersDTO(List<User> users);

    @InheritInverseConfiguration
    @Mapping(source = "bill.orders", target = "orders")
    @Mapping(target = "products_created", ignore = true)
    @Mapping(target = "categories_created", ignore = true)
    User toUser(UserDTO userDTO);
}
