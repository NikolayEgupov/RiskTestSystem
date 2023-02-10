package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.Student;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Integer> {
}
