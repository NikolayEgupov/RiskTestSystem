package ru.egupov.risktestsystem.controllers.test_exemp;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.models.Teacher;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.QuestionService;
import ru.egupov.risktestsystem.services.TestExempService;

@Controller
@RequestMapping("/test_inst/content")
public class ContentController {

    private final QuestionService questionService;
    private final TestExempService testExempService;

    public ContentController(QuestionService questionService, TestExempService testExempService) {
        this.questionService = questionService;
        this.testExempService = testExempService;
    }

    @GetMapping()
    public String listContent(@RequestParam(required = false, name = "test_id") Integer testId,
                                  Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        TestExemp testExemp = testExempService.findById(testId);

        model.addAttribute("questions", questionService.findBYTestExempId(testId));
        model.addAttribute("testE", testExemp);

        return "test_exemp/question/list";
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
