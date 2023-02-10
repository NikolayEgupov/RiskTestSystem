package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.repositories.GroupsRepository;
import ru.egupov.risktestsystem.repositories.TeacherRepository;

import java.util.List;

@Service
@Transactional
public class GroupService {

    private final GroupsRepository groupsRepository;
    private final TeacherService teacherService;

    public GroupService(GroupsRepository groupsRepository, TeacherService teacherService) {
        this.groupsRepository = groupsRepository;
        this.teacherService = teacherService;
    }

    public List<Group> findAll(){
        return groupsRepository.findAll();
    }

    public Group getById(int id){
        return groupsRepository.getById(id);
    }

    public void add(Group group){
        Teacher teacher = teacherService.findById(group.getTeacher().getId());
        group.setTeacher(teacher);
        groupsRepository.save(group);
    }

    public void update(int id, Group group){
        Teacher teacher = teacherService.findById(group.getTeacher().getId());
        group.setTeacher(teacher);
        group.setId(id);
        groupsRepository.save(group);
    }

    public void deleteById(int id){
        groupsRepository.deleteById(id);
    }
}
