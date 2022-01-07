package com.study.springjwt.domain;

import com.study.springjwt.repository.RoleRepository;
import com.study.springjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("user 생성 test")
    public void createUser(){
        Role manager = new Role();
        manager.setType(RoleType.ROLE_MANAGER);
        Role user = new Role();
        user.setType(RoleType.ROLE_USER);

        User newUser = User.createUser("test", "dd", manager, user);
        User save = userRepository.save(newUser);

        User test = userRepository.findUserWithAllByUsername("test");
        assertNotNull(test.getRoles());
        test.getRoles().forEach(i->System.out.println(i.getType()));

    }
}