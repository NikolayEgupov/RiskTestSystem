package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;

import java.util.List;

@Repository
public interface TestAccessRepository extends JpaRepository<TestAccess, Integer> {

    public List<TestAccess> findByTestExemp(TestExemp testExemp);

    @Query(value = "select t from TestAccess t where t.testExemp = ?1 and (t.countAccess - t.countUse) >= 1 order by t.student.group.name")
    public List<TestAccess> findByTestExempForUse(TestExemp testExemp);
}
