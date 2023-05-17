package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.Question;
import ru.egupov.risktestsystem.models.TestExemp;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Integer> {

    List<Question> findByTestExemp (TestExemp testExemp);

}
