package ru.egupov.risktestsystem.controllers.content.Attempt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.AttemptService;
import ru.egupov.risktestsystem.services.TestAccessService;

@Controller
@RequestMapping("/content/attempt")
public class AttemptController {

    private final AttemptService attemptService;
    private final TestAccessService testAccessService;

    public AttemptController(AttemptService attemptService, TestAccessService testAccessService) {
        this.attemptService = attemptService;
        this.testAccessService = testAccessService;
    }

    @GetMapping("/open/{id}")
    public String openContent(@PathVariable("id") int id,
                              Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        int res = attemptService.checkOpenAccess(id);
        TestAccess testAccess = testAccessService.findById(id);

        model.addAttribute("testAccess", testAccess);
        model.addAttribute("res", res);

        return "student_pages/content/attempt/open";
    }

    static Student getStudentAuth(){
        Person person = getPersonAuth();
        Student student = null;
        if (person.getRole() == SysRole.ROLE_STUDENT) {
            student = (Student) person;
        }
        return student;
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
