package com.zapp.marketapp.controller;

import com.zapp.marketapp.config.security.utils.AuthResponse;
import com.zapp.marketapp.dto.UserDTO;
import com.zapp.marketapp.service.UserService;
import com.zapp.marketapp.utils.UserProfile;
import com.zapp.marketapp.utils.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @Secured({"ADMIN_ROLE"})
    @GetMapping
    public ResponseEntity<List<UserDTO>> users() {
        return ResponseEntity.ok(userService.users());
    }

    @Secured("ADMIN_ROLE")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Secured("ADMIN_ROLE")
    @PutMapping("role/{userId}")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable("userId") Integer id,
            @RequestBody @Valid UserRole newRole
    ) {
        return ResponseEntity.ok(userService.updateUserRole(id,newRole));
    }

    @PutMapping("profile/{userId}")
    public ResponseEntity<UserDTO> updateProfile(
            @PathVariable("userId") Integer id,
            @RequestBody @Valid UserProfile newChanges
    ) {
        return ResponseEntity.ok(userService.updateProfile(id,newChanges));
    }

    @Secured("ADMIN_ROLE")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable("userId") Integer id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @Secured("ADMIN_ROLE")
    @PutMapping("/restore-user/{userId}")
    public ResponseEntity<Map<String,Object>> restoreUser(@PathVariable("userId") Integer id) {
        return ResponseEntity.ok(userService.restoreUser(id));
    }

}
