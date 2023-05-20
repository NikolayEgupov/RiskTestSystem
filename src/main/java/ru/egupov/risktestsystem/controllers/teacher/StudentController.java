package ru.egupov.risktestsystem.controllers.teacher;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.GroupService;
import ru.egupov.risktestsystem.services.StudentService;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final GroupService groupService;
    private final StudentService studentService;

    public StudentController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping()
    public String listPage(Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        model.addAttribute("groups", groupService.findByTeacher(teacher));
        model.addAttribute("students", studentService.findByGroup_Teacher(teacher));

        return "teacher_pages/student/list";
    }

    static Teacher getTeacherAuth(){
        Person person = getPersonAuth();
        Teacher teacher = null;
        if (person.getRole() == SysRole.ROLE_TEACHER) {
            teacher = (Teacher) person;
        }
        return teacher;
    }

    static Person getPersonAuth(){

        Person personAuth = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){

            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            personAuth = personDetails.getPerson();

        }

        return personAuth;
    }
}
