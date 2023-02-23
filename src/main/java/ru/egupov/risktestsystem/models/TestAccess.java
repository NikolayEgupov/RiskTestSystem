package ru.egupov.risktestsystem.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "test_access")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TestAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "test_exemp_id")
    TestExemp testExemp;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Student student;

    @Column(name = "time_limit")
    int timeLimit;

    @Column(name = "dt_start")
    @Temporal(TemporalType.TIMESTAMP)
    Date dateStart;

    @Column(name = "dt_end")
    @Temporal(TemporalType.DATE)
    Date dateEnd;

    @Column(name = "count_access")
    int countAccess;

    @Column(name = "count_use")
    int countUse;
}
