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

    @Column(name = "priority")
    int priority;

    @Column(name = "result")
    String result;

    @Transient
    float countBall;

    public float getCountBall() {
        long countCorrectVariant = question.getVariants().stream().filter(Variant::isCorrect).count();
        long countSolution = 0;
        for (Variant variant: question.getVariants()) {
            if (variant.isCorrect() && result.contains(variant.getId() + ",")){
                countSolution++;
            }
        }
        System.out.println("(countSolution/countCorrectVariant) " + (countSolution/countCorrectVariant));
        System.out.println("question.getMaxCount() " + question.getMaxCount());
        countBall = ((float) countSolution/countCorrectVariant)*question.getMaxCount();
        return countBall;
    }
}
