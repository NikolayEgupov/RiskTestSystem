package ru.egupov.risktestsystem.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.services.GroupService;
import ru.egupov.risktestsystem.services.StudentService;

import java.util.List;

@Controller
@RequestMapping("/admin/student")
public class StudentAdminController {

    private final GroupService groupService;
    private final StudentService studentService;

    public StudentAdminController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping("/add")
    public String addPage(@RequestParam(required = false, name = "group_id") String groupId,
                          Model model){

        Student student = new Student();
        List<Group> groups = groupService.findAll();
        Group group = new Group();

        if (groupId != null && groupId.chars().allMatch( Character::isDigit )){
            group = groupService.getById(Integer.parseInt(groupId));
            student.setGroup(group);

        }

        model.addAttribute("student", student);
        model.addAttribute("groups", groups);

        return "admin/add_student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student){

        studentService.add(student);

        return "redirect:/admin/group/edit/" + student.getGroup().getId() + "?edit";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                           Model model){
        Student student = studentService.findById(id);
        List<Group> groups = groupService.findAll();
        model.addAttribute("student", student);
        model.addAttribute("groups", groups);

        return "admin/edit_student";
    }

    @PatchMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") int id,
                              @ModelAttribute("student") Student student){

        studentService.update(id, student);

        return "redirect:/admin/student/edit/" + id + "?edit";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                             Model model){

        Student student = studentService.findById(id);
        model.addAttribute("student", true);
        model.addAttribute("person", student);

        return "admin/delete_user";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id){

        int group_id = studentService.findById(id).getGroup().getId();

        studentService.deleteById(id);

        return "redirect:/admin/group/edit/" + group_id + "?edit";
    }
}
