package com.study.springjwt.repository;

import com.study.springjwt.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(value = "User.withAll")
    User findUserWithAllByUsername(String username);
}
