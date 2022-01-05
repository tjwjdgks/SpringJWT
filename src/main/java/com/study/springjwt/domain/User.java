package com.study.springjwt.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Entity @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "User.withAll",attributeNodes = {
        @NamedAttributeNode("roles")
})
public class User {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    public static User createUser(String username, String password, Role ... roles){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        for(Role role : roles){
            user.addRole(role);
        }
        user.setCreateAt(LocalDateTime.now());
        return user;
    }

    public void addRole(Role role){
        roles.add(role);
        role.setUser(this);
    }
}
