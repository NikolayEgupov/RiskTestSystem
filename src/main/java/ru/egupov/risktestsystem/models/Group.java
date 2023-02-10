package ru.egupov.risktestsystem.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_group")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE )
    private List<Student> students;
}
