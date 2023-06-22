package ru.egupov.risktestsystem.controllers.teacher;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.GroupService;
import ru.egupov.risktestsystem.services.PersonService;
import ru.egupov.risktestsystem.services.StudentService;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final GroupService groupService;
    private final StudentService studentService;

    private final PersonService personService;

    public StudentController(GroupService groupService, StudentService studentService, PersonService personService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.personService = personService;
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

    @GetMapping("/add")
    public String addPage(@RequestParam(required = false, name = "group_id") String groupId,
                          Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        Student student = new Student();
        List<Group> groups = groupService.findAll();
        Group group = new Group();

        if (groupId != null && groupId.chars().allMatch( Character::isDigit )){
            group = groupService.getById(Integer.parseInt(groupId));
            student.setGroup(group);

        }

        model.addAttribute("student", student);
        model.addAttribute("groups", groups);

        return "teacher_pages/student/add_student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student){

        studentService.add(student);

        return "redirect:/student?add";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                           Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        Student student = studentService.findById(id);
        List<Group> groups = groupService.findByTeacher(teacher);
        model.addAttribute("student", student);
        model.addAttribute("groups", groups);

        return "teacher_pages/student/edit_student";
    }

    @PatchMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") int id,
                              @ModelAttribute("student") Student student){

        studentService.update(id, student);

        return "redirect:/student?edit";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                             Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        Student student = studentService.findById(id);
        model.addAttribute("student", true);
        model.addAttribute("person", student);

        return "teacher_pages/student/delete_user";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        int group_id = studentService.findById(id).getGroup().getId();

        studentService.deleteById(id);

        return "redirect:/student?delete";
    }

    @GetMapping("/new_pass/{id}")
    public String newPass(@PathVariable("id") int id){
        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        personService.newPass(id);
        return "redirect:/student/edit/" + id + "?pass";
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
