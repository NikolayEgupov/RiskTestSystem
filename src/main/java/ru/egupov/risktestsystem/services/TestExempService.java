package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.repositories.TestExempRepository;

import java.util.List;

@Service
@Transactional
public class TestExempService {

    private final TestExempRepository testExempRepository;
    private final TeacherService teacherService;


    public TestExempService(TestExempRepository testExempRepository, TeacherService teacherService) {
        this.testExempRepository = testExempRepository;
        this.teacherService = teacherService;
    }

    public TestExemp findById(int id){
        return testExempRepository.findById(id).orElse(null);
    }

    public List<TestExemp> findByTeacherOrderByIdDesc(Teacher teacher){
        return testExempRepository.findByTeacherOrderByIdDesc(teacher);
    }

    public void save(TestExemp testExemp){

        Teacher teacher = teacherService.findById(testExemp.getTeacher().getId());
        testExemp.setTeacher(teacher);

        testExempRepository.save(testExemp);
    }

    public void update(int id, TestExemp testExemp){
        testExemp.setId(id);
        save(testExemp);
    }

    public void deleteById(int id){
        testExempRepository.deleteById(id);
    }
}
