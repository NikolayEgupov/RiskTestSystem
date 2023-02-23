package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.models.TestExemp;

import java.util.List;

@Repository
public interface TestExempRepository extends JpaRepository<TestExemp, Integer> {

    public List<TestExemp> findByTeacherOrderByIdDesc(Teacher teacher);
}
