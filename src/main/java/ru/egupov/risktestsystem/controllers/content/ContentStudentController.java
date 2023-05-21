package ru.egupov.risktestsystem.controllers.content;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.egupov.risktestsystem.models.Attempt;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.AttemptService;
import ru.egupov.risktestsystem.services.TestAccessService;
import ru.egupov.risktestsystem.services.TestExempService;

import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentStudentController {

    private final TestExempService testExempService;
    private final TestAccessService testAccessService;

    private final AttemptService attemptService;
    public ContentStudentController(TestExempService testExempService, TestAccessService testAccessService, AttemptService attemptService) {
        this.testExempService = testExempService;
        this.testAccessService = testAccessService;
        this.attemptService = attemptService;
    }

    @GetMapping()
    public String listContent(Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        List<TestAccess> testAccess = testAccessService.findActualByStudent(student);
        List<Attempt> attempts = attemptService.findAllArchive(student);
        System.out.println(testAccess.size());
        model.addAttribute("testAccess", testAccess);
        model.addAttribute("attempts", attempts);

        return "student_pages/content/list";
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
