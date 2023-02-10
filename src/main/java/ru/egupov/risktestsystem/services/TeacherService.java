package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.repositories.TeacherRepository;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PersonService personService;

    public TeacherService(TeacherRepository teacherRepository, PersonService personService) {
        this.teacherRepository = teacherRepository;
        this.personService = personService;
    }

    public List<Teacher> findAll(){
        return teacherRepository.findAll();
    }

    public Teacher findById(int id){
        return teacherRepository.findById(id).orElse(null);
    }

    public void add(Teacher teacher){
        newPassword(teacher);
        save(teacher);
    }

    public void update(int id, Teacher teacher){
        Teacher teacherOld = findById(id);
        teacher.setId(id);
        teacher.setPassword(teacherOld.getPassword());
        teacherRepository.save(teacher);
    }

    public void newPass(int id){
        Teacher teacher = findById(id);
        newPassword(teacher);
        teacherRepository.save(teacher);
    }

    public void deleteById(int id){
        teacherRepository.deleteById(id);
    }

    public void newPassword(Teacher teacher){
        personService.registration(teacher);
    }

    public void save(Teacher teacher){
        teacherRepository.save(teacher);
    }
}
