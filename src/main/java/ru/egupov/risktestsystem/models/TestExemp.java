package ru.egupov.risktestsystem.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.egupov.risktestsystem.utils.TypeViewReview;

import javax.persistence.*;

@Entity
@Table(name = "test_exemp")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TestExemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "type_view_review")
    @Enumerated(EnumType.STRING)
    TypeViewReview typeViewReview;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    Teacher teacher;

}
