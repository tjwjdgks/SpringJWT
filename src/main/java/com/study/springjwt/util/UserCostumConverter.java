package com.study.springjwt.util;

import com.study.springjwt.domain.Role;
import com.study.springjwt.domain.RoleType;
import com.study.springjwt.domain.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserCostumConverter {

    public User mapToUser(Map<String,Object> map){
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        List<String> list = (List<String>) map.get("roles");
        List<Role> roles = list.stream()
                .filter(i -> Arrays.stream(RoleType.values()).map(e -> e.name()).anyMatch(e -> e.equals(i)))
                .map(r -> new Role(RoleType.valueOf(r)))
                .collect(Collectors.toList());
        User user = User.createUser(username, password, roles.toArray(new Role[0]));
        return user;
    }

}
