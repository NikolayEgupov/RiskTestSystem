package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.egupov.risktestsystem.models.Solution;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {
}