package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.TestExemp;

@Repository
public interface TestExempRepository extends JpaRepository<TestExemp, Integer> {
}
