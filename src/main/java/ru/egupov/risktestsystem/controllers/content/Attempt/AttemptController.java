package ru.egupov.risktestsystem.controllers.content.Attempt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.models.*;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.AttemptService;
import ru.egupov.risktestsystem.services.SolutionService;
import ru.egupov.risktestsystem.services.TestAccessService;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/content/attempt")
public class AttemptController {

    private final AttemptService attemptService;
    private final TestAccessService testAccessService;

    private final SolutionService solutionService;

    public AttemptController(AttemptService attemptService, TestAccessService testAccessService, SolutionService solutionService) {
        this.attemptService = attemptService;
        this.testAccessService = testAccessService;
        this.solutionService = solutionService;
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

    @PostMapping("/open/{id}")
    public String open(@PathVariable("id") int id,
                              Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        TestAccess testAccess = testAccessService.findById(id);
        Attempt attempt = attemptService.openAccess(testAccess);
        String res = "common/error";
        if (attempt != null){
            res = "redirect:/content/attempt/" + attempt.getId();
        }

        return res;
    }

    @GetMapping("/{id}")
    public String attemptRunPage(@PathVariable("id") int id,
                              Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        Attempt attempt = attemptService.findById(id);
        TestAccess testAccess = attempt.getTestAccess();
        Date dateEnd = new Date(attempt.getDateStart().getTime()
                + (long) testAccess.getTimeLimit() * 60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyyy HH:mm:ss");
        String dtEndFormat = simpleDateFormat.format(dateEnd);

        model.addAttribute("testAccess", testAccess);
        model.addAttribute("attempt", attempt);
        model.addAttribute("solutions", attempt.getSolutions().stream().sorted(Comparator.comparing(Solution::getPriority)).collect(Collectors.toList()));
        model.addAttribute("dtEndFormat", dtEndFormat);

        return "student_pages/content/attempt/run";
    }

    @GetMapping("/{id_attempt}/solution/{id_solution}")
    public String attemptProcessPage(@PathVariable("id_attempt") int id_attempt,
                                     @PathVariable("id_solution") int id_solution,
                                 Model model){

        Attempt attempt = attemptService.findById(id_attempt);
        Solution solution = solutionService.findById(id_solution);
        TestAccess testAccess = attempt.getTestAccess();
        Date dateEnd = new Date(attempt.getDateStart().getTime()
                + (long) testAccess.getTimeLimit() * 60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyyy HH:mm:ss");
        String dtEndFormat = simpleDateFormat.format(dateEnd);

        if (attemptService.checkProcessAttempt(attempt) == -1){
            return "redirect:/content?proc_end_time";
        }

        int countCorrect = (int) solution.getQuestion().getVariants().stream().filter(Variant::isCorrect).count();

        model.addAttribute("solution", solution);
        model.addAttribute("attempt", attempt);
        model.addAttribute("countCorrect", countCorrect);
        model.addAttribute("dtEndFormat", dtEndFormat);

        return "student_pages/content/attempt/process";
    }

    @PostMapping ("/{id_attempt}/solution/{id_solution}")
    public String attemptProcess(@PathVariable("id_attempt") int id_attempt,
                                 @PathVariable("id_solution") int id_solution,
                                 @ModelAttribute("solution") Solution solution){
        Solution solutionBase = solutionService.findById(id_solution);
        solutionBase.setResult(solution.getResult() + ",");
        solutionService.save(solutionBase);
        Solution solutionNext = solutionService.getNext(solutionBase);
        String resStr;
        if (solutionNext == null)
            resStr = "redirect:/content/attempt/close/" + solutionBase.getAttempt().getId();
        else
            resStr = "redirect:/content/attempt/" + solutionNext.getAttempt().getId()
                    + "/solution/"+solutionNext.getId();


        return resStr;
    }

    @GetMapping("/close/{id}")
    public String attemptClosePage(@PathVariable("id") int id,
                                 Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        Attempt attempt = attemptService.findById(id);
        TestAccess testAccess = attempt.getTestAccess();
        Date dateEnd = new Date(attempt.getDateStart().getTime()
                + (long) testAccess.getTimeLimit() * 60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyyy HH:mm:ss");
        String dtEndFormat = simpleDateFormat.format(dateEnd);

        model.addAttribute("testAccess", testAccess);
        model.addAttribute("attempt", attempt);
        model.addAttribute("dtEndFormat", dtEndFormat);

        return "student_pages/content/attempt/close";
    }

    @PostMapping("/close/{id}")
    public String attemptClose(@PathVariable("id") int id,
                                   Model model){

        Student student = getStudentAuth();

        if (student == null)
            return "common/error";

        Attempt attempt = attemptService.findById(id);
        TestAccess testAccess = attempt.getTestAccess();
        attemptService.closeAttempt(attempt, new Date());

        return "redirect:/content?proc_end";
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
