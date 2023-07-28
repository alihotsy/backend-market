package com.zapp.marketapp.service;

import com.zapp.marketapp.config.security.JwtAuthService;
import com.zapp.marketapp.config.security.utils.AuthResponse;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.dto.mapper.UserMapper;
import com.zapp.marketapp.entities.User;
import com.zapp.marketapp.exceptions.EntityException;
import com.zapp.marketapp.helpers.CurrentAuthUser;
import com.zapp.marketapp.repository.JpaUser;
import com.zapp.marketapp.utils.Roles;
import com.zapp.marketapp.utils.UserProfile;
import com.zapp.marketapp.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService implements CommandLineRunner {

    private final UserMapper userMapper;

    private final JwtAuthService jwtAuthService;
    private final JpaUser jpaUser;

    private final PasswordEncoder passwordEncoder;
    private final CurrentAuthUser currentAuthUser;

    public List<UserDTO> users() {
        return userMapper.toUsersDTO(jpaUser.findAll());
    }
    public AuthResponse createUser(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User data = userMapper.toUser(userDTO);
        data.setRole(Roles.CLIENT_ROLE);
        jpaUser.findByEmail(data.getEmail())
                .ifPresent(user -> {
                    throw new EntityException(
                            false,
                            "Ya existe un usuario con ese email",
                            "email",
                            400,
                            "User"
                    );
                });



        UserDTO newUser = userMapper.toUserDTO(jpaUser.save(data));

        return AuthResponse.builder()
                .user(newUser)
                .token(jwtAuthService.generateToken(newUser,Map.of("uid",newUser.getUserId())))
                .build();
    }

    public UserDTO updateUserRole(Integer userId, UserRole newChanges) {
       //TODO: Rol administrador no puede actualizar su rol ni el de otros administradores.
       return jpaUser.findByUserIdAndState(userId,true)
                .map(userFound -> {
                    userFound.setRole(newChanges.getRole());
                    return userMapper.toUserDTO(jpaUser.save(userFound));
                }).orElseThrow(
                        () ->  new EntityException(
                                false,
                                "No existe el usuario con ese ID",
                                "path_variable",
                                404,
                                "User"
                        )
                );
    }

    public UserDTO updateProfile(Integer id, UserProfile newChanges) {

        UserDTO authUser = currentAuthUser.getUserAuthenticated();

       return jpaUser.findByUserIdAndState(id,true)
                .map(user -> {

                    if(!Objects.equals(authUser.getUserId(), id)) {
                        throw new EntityException(false,"Este perfil es privado","path_variable",401,"User");
                    }

                    if(user.isGoogle() && !user.getEmail().equals(newChanges.getEmail())) {
                            throw new EntityException(
                                    false,
                                    "Usuarios de Google no pueden actualizar el email",
                                    "email",
                                    400,
                                    "User"
                            );
                    }

                    jpaUser.findByEmail(newChanges.getEmail())
                            .ifPresent(userFound -> {
                                if(!id.equals(userFound.getUserId())) {
                                    throw new EntityException(
                                            false,
                                            "El email ya existe en otro usuario",
                                            "email",
                                            400,
                                            "User"
                                    );
                                }

                            });
                    user.setName(newChanges.getName());
                    user.setEmail(newChanges.getEmail());
                    return userMapper.toUserDTO(jpaUser.save(user));
                }).orElseThrow(
                        () ->  new EntityException(
                                false,
                                "No existe el usuario con ese ID",
                                "path_variable",
                                404,
                                "User"
                        )
                );
    }

    public Map<String,Object> deleteUser(Integer id) {
        //TODO: El usuario administrador no puede eliminarse así mismo ni a otros admins.
      return jpaUser.findByUserIdAndState(id,true)
                .map(user -> {
                    Map<String,Object> resp = new HashMap<>();
                    resp.put("ok",true);
                    resp.put("msg","Usuario eliminado satisfactoriamente!");
                    user.setState(false);
                    jpaUser.save(user);
                    return resp;
                }).orElseThrow(
                      () ->  new EntityException(
                              false,
                              "No existe el usuario con ese ID",
                              "path_variable",
                              404,
                              "User"
                      )
              );
    }

    public Map<String, Object> restoreUser(Integer id) {
       return jpaUser.findById(id).map(user -> {
            Map<String,Object> resp = new HashMap<>();

            if(user.isState()) {
                resp.put("ok",true);
                resp.put("msg","El usuario actualmente está activo!");
                resp.put("restored", false);
                return resp;
            }

            user.setState(true);
            jpaUser.save(user);
            resp.put("ok",true);
            resp.put("msg","Usuario recuperado satisfactoriamente!");
            resp.put("restored", true);
            return resp;
        }).orElseThrow(
                () ->  new EntityException(
                        false,
                        "No existe el usuario con ese ID",
                        "path_variable",
                        404,
                        "User"
                )
        );
    }

    public UserDTO getUserById(Integer id) {
        return jpaUser.findByUserIdAndState(id,true)
                .map(userMapper::toUserDTO)
                .orElseThrow(
                        () ->  new EntityException(
                                false,
                                "No existe el usuario con ese ID",
                                "path_variable",
                                404,
                                "User"
                        )
                );
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(passwordEncoder.encode("123456"));
    }
}
