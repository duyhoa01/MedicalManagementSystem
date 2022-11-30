package com.medical.repository;

import com.medical.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String username);

    @Query("FROM User u WHERE u.token= ?1 AND u.status=false")
    User findByToken(String token);

    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM User s " +
            "WHERE s.email = ?1"
    )
    Boolean selectExistsUserName(String username);
}
