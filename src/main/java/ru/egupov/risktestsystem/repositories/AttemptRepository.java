package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egupov.risktestsystem.models.Attempt;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    List<Attempt> findByTestAccess(TestAccess testAccess);

    List<Attempt> findByTestAccessStudentAndAndDateEndIsNotNull(Student student);

    List<Attempt> findByTestAccessTestExempAndDateEndIsNotNull(TestExemp testExemp);
}