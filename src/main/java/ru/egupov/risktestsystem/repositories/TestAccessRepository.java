package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;

import java.util.List;

@Repository
public interface TestAccessRepository extends JpaRepository<TestAccess, Integer> {
    public List<TestAccess> findByTestExempAndDateUseIsNull(TestExemp testExemp);
}
