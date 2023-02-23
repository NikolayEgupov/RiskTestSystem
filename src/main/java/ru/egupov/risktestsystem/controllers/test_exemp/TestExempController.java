package ru.egupov.risktestsystem.controllers.test_exemp;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.TestAccessService;
import ru.egupov.risktestsystem.services.TestExempService;
import ru.egupov.risktestsystem.utils.TypeViewReview;

import java.util.List;

@Controller
@RequestMapping("/test_inst")
public class TestExempController {

    private final TestExempService testExempService;
    private final TestAccessService testAccessService;

    public TestExempController(TestExempService testExempService, TestAccessService testAccessService) {
        this.testExempService = testExempService;
        this.testAccessService = testAccessService;
    }

    @GetMapping()
    public String allTestsInst(Model model){

        Person person = getPersonAuth();
        Teacher teacher = null;
        if (person.getRole() == SysRole.ROLE_TEACHER) {
            teacher = (Teacher) person;
        }

        List<TestExemp> testExemps = testExempService.findByTeacherOrderByIdDesc(teacher);

        model.addAttribute("testExemps", testExemps);

        return "test_exemp/list_for_edit";
    }

    @GetMapping("/add")
    public String addPage(Model model){

        Teacher teacher = getTeacherAuth();

        TestExemp testExemp = new TestExemp();
        testExemp.setTeacher(teacher);
        testExemp.setTypeViewReview(TypeViewReview.FullReview);

        model.addAttribute("testE", testExemp);

        return "test_exemp/add";

    }

    @PostMapping("/add")
    public String add(@ModelAttribute("testExemp") TestExemp testExemp){

        if (getTeacherAuth() == null)
            return "common/error";

        testExempService.save(testExemp);

        return "redirect:/test_inst";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                           Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        TestExemp testExemp = testExempService.findById(id);
        List<TestAccess> testAccesses = testAccessService.findByTestExempAndDateUseIsNull(testExemp);

        model.addAttribute("testE", testExemp);
        model.addAttribute("testAccess", testAccesses);

        return "test_exemp/edit";

    }

    @PatchMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("testE") TestExemp testExemp){
        if (getTeacherAuth() == null)
            return "common/error";

        testExempService.update(id, testExemp);

        return "redirect:/test_inst";

    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") int id,
                           Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        TestExemp testExemp = testExempService.findById(id);

        model.addAttribute("testE", testExemp);

        return "test_exemp/delete";

    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id,
                             Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        testExempService.deleteById(id);

        return "redirect:/test_inst";
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
