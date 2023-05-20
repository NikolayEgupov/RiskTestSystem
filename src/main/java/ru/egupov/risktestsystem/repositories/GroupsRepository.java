package ru.egupov.risktestsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Teacher;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Integer> {

    List<Group> findByTeacher(Teacher teacher);
}
