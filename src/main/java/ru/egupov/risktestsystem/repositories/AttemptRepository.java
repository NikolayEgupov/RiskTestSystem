package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egupov.risktestsystem.models.Attempt;
import ru.egupov.risktestsystem.models.TestAccess;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    List<Attempt> findByTestAccess(TestAccess testAccess);
}