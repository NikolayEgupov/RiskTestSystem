package ru.egupov.risktestsystem.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.egupov.risktestsystem.security.SysRole;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@ToString
public class Teacher extends Person{

    @Column(name = "tel")
    private String tel;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Group> groups;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private List<TestExemp> testExemps;

    public Teacher() {
        this.setRole(SysRole.ROLE_TEACHER);
    }

    public Teacher(String name, String email, String password, String tel) {
        super(name, email, password, SysRole.ROLE_TEACHER);
        this.tel = tel;
    }
}
