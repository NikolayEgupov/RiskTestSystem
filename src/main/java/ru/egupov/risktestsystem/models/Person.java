package ru.egupov.risktestsystem.models;

import lombok.*;
import ru.egupov.risktestsystem.security.SysRole;

import javax.persistence.*;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private SysRole role;

    public Person(String name, String email, String password, SysRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
