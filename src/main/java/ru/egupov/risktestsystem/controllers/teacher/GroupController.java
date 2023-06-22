package ru.egupov.risktestsystem.controllers.teacher;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.GroupService;
import ru.egupov.risktestsystem.services.TeacherService;

import java.util.List;

@Controller
@RequestMapping("/group")
public class GroupController {

    private final TeacherService teacherService;
    private final GroupService groupService;

    public GroupController(TeacherService teacherService, GroupService groupService) {
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String listPage(Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        model.addAttribute("groups", groupService.findByTeacher(teacher));

        return "teacher_pages/group/list";
    }

    @GetMapping("/add")
    public String addPage(Model model){
        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        Group group = new Group();
        group.setTeacher(teacher);

        model.addAttribute("teacher", teacher);
        model.addAttribute("group", group);

        return "teacher_pages/group/add_group";
    }

    @PostMapping("/add")
    public String addGroup(@ModelAttribute("group") Group group){
        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";
        group.setTeacher(teacher);
        groupService.add(group);
        return "redirect:/group?add";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                           Model model){
        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";
        Group group = groupService.getById(id);

        model.addAttribute("group", group);

        return "teacher_pages/group/edit_group";
    }

    @PatchMapping("/edit/{id}")
    public String editGroup(@PathVariable("id") int id,
                            @ModelAttribute("group") Group group){
        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";
        group.setTeacher(teacher);
        groupService.update(id, group);
        return "redirect:/group?edit";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                             Model model){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        Group group = groupService.getById(id);

        model.addAttribute("group", group);

        return "teacher_pages/group/delete_group";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") int id){

        Teacher teacher = getTeacherAuth();

        if (teacher == null)
            return "common/error";

        groupService.deleteById(id);

        return "redirect:/group?delete";
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
