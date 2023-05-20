package ru.egupov.risktestsystem.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.egupov.risktestsystem.security.SysRole;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
public class Student extends Person{

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TestAccess> testAccesses;

    public Student() {
        this.setRole(SysRole.ROLE_STUDENT);
    }

    public Student(String name, String email, String password, Group group) {
        super(name, email, password, SysRole.ROLE_STUDENT);
        this.group = group;
    }
}
