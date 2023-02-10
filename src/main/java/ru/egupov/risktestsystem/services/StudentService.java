package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.repositories.StudentsRepository;

@Service
@Transactional
public class StudentService {

    private final StudentsRepository studentsRepository;
    private final PersonService personService;
    private final GroupService groupService;

    public StudentService(StudentsRepository studentsRepository, PersonService personService, GroupService groupService) {
        this.studentsRepository = studentsRepository;
        this.personService = personService;
        this.groupService = groupService;
    }

    public Student findById (int id){
        return studentsRepository.findById(id).orElse(null);
    }

    public void add(Student student){
        newPassword(student);
        save(student);
    }

    public void update(int id, Student student){
        Group group = groupService.getById(student.getGroup().getId());
        Student studentOld = findById(id);
        student.setPassword(studentOld.getPassword());
        student.setGroup(group);
        student.setId(id);
        studentsRepository.save(student);
    }

    public void deleteById(int id){
        studentsRepository.deleteById(id);
    }

    public void save(Student student){
        studentsRepository.save(student);
    }

    public void newPassword(Student student){
        personService.registration(student);
    }
}
