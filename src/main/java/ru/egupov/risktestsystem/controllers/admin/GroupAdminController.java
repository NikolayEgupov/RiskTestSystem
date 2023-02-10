package ru.egupov.risktestsystem.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.services.GroupService;
import ru.egupov.risktestsystem.services.TeacherService;

import java.util.List;

@Controller
@RequestMapping("/admin/group")
public class GroupAdminController {

    private final TeacherService teacherService;
    private final GroupService groupService;

    public GroupAdminController(TeacherService teacherService, GroupService groupService) {
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping("/add")
    public String addPage(@RequestParam(required = false, name = "teacher_id") String teacherId,
                          Model model){
        Group group = new Group();
        Teacher teacher = new Teacher();
        List<Teacher> teachers = teacherService.findAll();
        if (teacherId != null && teacherId.chars().allMatch( Character::isDigit )){
            teacher = teacherService.findById(Integer.parseInt(teacherId));
            group.setTeacher(teacher);

        }
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacher", teacher);
        model.addAttribute("group", group);

        return "admin/add_group";
    }

    @PostMapping("/add")
    public String addGroup(@ModelAttribute("group") Group group){
        groupService.add(group);
        return "redirect:/admin/teacher/edit/" + group.getTeacher().getId();
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                           Model model){

        Group group = groupService.getById(id);
        List<Teacher> teachers = teacherService.findAll();

        model.addAttribute("group", group);
        model.addAttribute("teachers", teachers);

        return "admin/edit_group";
    }

    @PatchMapping("/edit/{id}")
    public String editGroup(@PathVariable("id") int id,
                            @ModelAttribute("group") Group group){
        groupService.update(id, group);
        return "redirect:/admin/teacher/edit/" + group.getTeacher().getId();
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                           Model model){

        Group group = groupService.getById(id);

        model.addAttribute("group", group);

        return "admin/delete_group";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") int id){

        int teacher_id = groupService.getById(id).getTeacher().getId();

        groupService.deleteById(id);

        return "redirect:/admin/teacher/edit/" + teacher_id;
    }
}
