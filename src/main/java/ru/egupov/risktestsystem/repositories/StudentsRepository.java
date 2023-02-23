package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Student;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Integer> {
    public List<Student> findByGroup(Group group);
}
