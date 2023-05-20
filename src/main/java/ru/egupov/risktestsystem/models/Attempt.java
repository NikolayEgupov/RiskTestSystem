package ru.egupov.risktestsystem.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "attempt")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "test_access_id")
    TestAccess testAccess;

    @Column(name = "dt_start")
    @Temporal(TemporalType.TIMESTAMP)
    Date dateStart;

    @Column(name = "dt_end")
    @Temporal(TemporalType.DATE)
    Date dateEnd;

    @Column(name = "result")
    int result;

    @OneToMany(mappedBy = "attempt")
    List<Solution> solutions;
}
