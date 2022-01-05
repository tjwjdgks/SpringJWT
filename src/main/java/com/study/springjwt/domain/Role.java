package com.study.springjwt.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Role {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Role(RoleType type) {
        this.type = type;
    }
}
