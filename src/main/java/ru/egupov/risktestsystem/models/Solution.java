package ru.egupov.risktestsystem.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "solution")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    Attempt attempt;

    @ManyToOne
    @JoinColumn(name = "question_id")
    Question question;

    @Column(name = "result")
    String result;
}
