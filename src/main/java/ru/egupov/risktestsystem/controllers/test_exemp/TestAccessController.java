package ru.egupov.risktestsystem.controllers.test_exemp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.DTO.TestAccessGroupDTO;
import ru.egupov.risktestsystem.DTO.TestAccessStudentDTO;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.TestAccessGroupDTOService;
import ru.egupov.risktestsystem.services.TestAccessStudentDTOService;
import ru.egupov.risktestsystem.services.TestExempService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test_access")
public class TestAccessController {

    private final TestExempService testExempService;
    private final TestAccessGroupDTOService testAccessGroupDTOService;

    private final TestAccessStudentDTOService testAccessStudentDTOService;

    public TestAccessController(TestExempService testExempService, TestAccessGroupDTOService testAccessGroupDTOService, TestAccessStudentDTOService testAccessStudentDTOService) {
        this.testExempService = testExempService;
        this.testAccessGroupDTOService = testAccessGroupDTOService;
        this.testAccessStudentDTOService = testAccessStudentDTOService;
    }

    @GetMapping("/add_for_group")
    public String addForGroupPage(@RequestParam(required = false, name = "test_id") String testId,
                          Model model){

        if (TestExempController.getPersonAuth().getRole() == SysRole.ROLE_STUDENT)
            return "common/error";

        TestExemp testExemp = new TestExemp();
        if (testId != null && testId.chars().allMatch( Character::isDigit )){
            testExemp = testExempService.findById(Integer.parseInt(testId));
        } else {
            return "common/error";
        }
        List<Group> groups = testExemp.getTeacher().getGroups();
        TestAccessGroupDTO testAccessGroupDTO = new TestAccessGroupDTO();
        testAccessGroupDTO.setTestExemp(testExemp);

        model.addAttribute("testE", testExemp);
        model.addAttribute("testAccessDTO", testAccessGroupDTO);
        model.addAttribute("groups", groups);

        return "test_exemp/access/add_group";
    }

    @PostMapping("/add_for_group")
    public String addForGroup(@ModelAttribute("testAccessDTO") TestAccessGroupDTO testAccessGroupDTO){
        testAccessGroupDTOService.addAccess(testAccessGroupDTO);
        return "redirect:/test_inst/edit/" + testAccessGroupDTO.getTestExemp().getId() + "?access";
    }

    @GetMapping("/add_for_student")
    public String addForStudentPage(@RequestParam(required = false, name = "test_id") String testId,
                                  Model model){

        if (TestExempController.getPersonAuth().getRole() == SysRole.ROLE_STUDENT)
            return "common/error";

        TestExemp testExemp = new TestExemp();
        if (testId != null && testId.chars().allMatch( Character::isDigit )){
            testExemp = testExempService.findById(Integer.parseInt(testId));
        } else {
            return "common/error";
        }

        List<Student> students = new ArrayList<>();
        testExemp.getTeacher().getGroups()
                .forEach(x -> students.addAll(x.getStudents()));

        TestAccessStudentDTO testAccessStudentDTO = new TestAccessStudentDTO();
        testAccessStudentDTO.setTestExemp(testExemp);

        model.addAttribute("testE", testExemp);
        model.addAttribute("testAccessStudentDTO", testAccessStudentDTO);
        model.addAttribute("students", students);

        return "test_exemp/access/add_student";
    }

    @PostMapping("/add_for_student")
    public String addForStudent(@ModelAttribute("testAccessStudentDTO") TestAccessStudentDTO testAccessStudentDTO){
        testAccessStudentDTOService.addAccess(testAccessStudentDTO);
        return "redirect:/test_inst/edit/" + testAccessStudentDTO.getTestExemp().getId() + "?access";
    }
}
