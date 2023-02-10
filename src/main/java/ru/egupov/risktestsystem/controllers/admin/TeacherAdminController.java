package ru.egupov.risktestsystem.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.services.DefaultEmailService;
import ru.egupov.risktestsystem.services.TeacherService;

@Controller
@RequestMapping("/admin/teacher")
public class TeacherAdminController {

    private final TeacherService teacherService;

    public TeacherAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping()
    public String mainAdminPage(Model model){
        model.addAttribute("teachers", teacherService.findAll());

        return "admin/teachers";
    }

    @GetMapping("/add")
    public String addPage(Model model){
        model.addAttribute("teacher", new Teacher());

        return "admin/add_teacher";
    }

    @PostMapping("/add")
    public String addTeacher(@ModelAttribute("teacher") Teacher teacher){

        teacherService.add(teacher);

        return "redirect:/admin/teacher?edit";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                             Model model){

        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);

        return "admin/edit_teacher";
    }

    @PatchMapping("/edit/{id}")
    public String editTeacher(@PathVariable("id") int id,
                              @ModelAttribute("teacher") Teacher teacher){

        teacherService.update(id, teacher);

        return "redirect:/admin/teacher?edit";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                             Model model){

        Teacher teacher = teacherService.findById(id);
        model.addAttribute("person", teacher);
        model.addAttribute("student", false);

        return "admin/delete_user";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable("id") int id){

        teacherService.deleteById(id);

        return "redirect:/admin/teacher?edit";
    }

    @GetMapping("/new_pass/{id}")
    public String newPass(@PathVariable("id") int id){
        teacherService.newPass(id);
        return "redirect:/admin/teacher/edit/" + id + "?pass";
    }

}
