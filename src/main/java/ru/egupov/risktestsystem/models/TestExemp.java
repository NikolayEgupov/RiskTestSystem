package ru.egupov.risktestsystem.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.egupov.risktestsystem.utils.TypeViewReview;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "testExemp")
    List<TestAccess> testAccesses;

    @OneToMany(mappedBy = "testExemp")
    List<Question> questions;

    @Transient
    int maxCount;

    public int getMaxCount() {
        return questions.stream().map(Question::getMaxCount).mapToInt(Integer::intValue).sum();
    }
}
