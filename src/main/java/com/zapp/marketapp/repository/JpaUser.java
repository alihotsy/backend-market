package com.zapp.marketapp.repository;

import com.zapp.marketapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUser extends JpaRepository<User,Integer> {
    Optional<User> findByUserIdAndState(Integer userId, boolean state);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndState(String email, boolean state);
    Optional<User> findByEmailAndStateAndGoogle(String email, boolean state, boolean google);

    @Query("SELECT u FROM User u WHERE u.state = ?3 and (UPPER(u.name) LIKE UPPER(CONCAT('%',?1,'%')) " +
            "or UPPER(u.email) LIKE UPPER(CONCAT('%',?2,'%')))")
    List<User> searchByNameOrEmail(String name,String email,boolean state);
}
