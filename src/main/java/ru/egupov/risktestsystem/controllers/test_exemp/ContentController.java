package ru.egupov.risktestsystem.controllers.test_exemp;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.*;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.QuestionService;
import ru.egupov.risktestsystem.services.TestExempService;
import ru.egupov.risktestsystem.services.VariantService;

@Controller
@RequestMapping("/test_inst/content")
public class ContentController {

    private final QuestionService questionService;
    private final TestExempService testExempService;

    private final VariantService variantService;

    public ContentController(QuestionService questionService, TestExempService testExempService, VariantService variantService) {
        this.questionService = questionService;
        this.testExempService = testExempService;
        this.variantService = variantService;
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

    @GetMapping("/add")
    public String addPage(@RequestParam(required = false, name = "test_id") Integer testId,
                              Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        TestExemp testExemp = testExempService.findById(testId);
        Question question = new Question();
        question.setTestExemp(testExemp);
        question.setMaxCount(1);

        model.addAttribute("question", question);
        model.addAttribute("testE", testExemp);

        return "test_exemp/question/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("question") Question question){

        if (getTeacherAuth() == null)
            return "common/error";

        System.out.println(question.getTestExemp());

        questionService.save(question);

        return "redirect:/test_inst/content?test_id=" + question.getTestExemp().getId();
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id,
                          Model model){

        if (getTeacherAuth() == null)
            return "common/error";


        Question question = questionService.findById(id);

        model.addAttribute("question", question);
        model.addAttribute("variants", question.getVariants());


        return "test_exemp/question/edit";
    }

    @PatchMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("question") Question question){

        if (getTeacherAuth() == null)
            return "common/error";

        questionService.update(id, question);

        return "redirect:/test_inst/content?test_id=" + question.getTestExemp().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){

        if (getTeacherAuth() == null)
            return "common/error";

        questionService.delete(id);

        return "redirect:/test_inst/";
    }

    @GetMapping("/add_variant")
    public String addVariantPage(@RequestParam(required = false, name = "question_id") Integer questionId,
                          Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        Question question = questionService.findById(questionId);
        Variant variant = new Variant();
        variant.setQuestion(question);
        variantService.save(variant);

        model.addAttribute("question", question);

        return "redirect:/test_inst/content/edit/" + questionId;
    }

    @GetMapping("/edit_variant/{id}")
    public String editVariantPage(@PathVariable("id") int id,
                           Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        Variant variant = variantService.findById(id);
        model.addAttribute("variant", variant);
        model.addAttribute("question", variant.getQuestion());

        return "test_exemp/variant/edit";
    }

    @PatchMapping("/edit_variant/{id}")
    public String editVariant(@PathVariable("id") int id,
                              @ModelAttribute("variant") Variant variant){

        if (getTeacherAuth() == null)
            return "common/error";

        variantService.update(id, variant);

        return "redirect:/test_inst/content/edit/" + variant.getQuestion().getId();
    }

    @GetMapping("/delete_variant/{id}")
    public String deleteVariantPage(@PathVariable("id") int id,
                                  Model model){

        if (getTeacherAuth() == null)
            return "common/error";

        Variant variant = variantService.findById(id);
        int idQuest = variant.getQuestion().getId();
        variantService.delete(id);

        return "redirect:/test_inst/content/edit/" + idQuest;
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
