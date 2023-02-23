package ru.egupov.risktestsystem.controllers.test_exemp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egupov.risktestsystem.DTO.TestAccessGroupDTO;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.security.SysRole;
import ru.egupov.risktestsystem.services.TestAccessGroupDTOService;
import ru.egupov.risktestsystem.services.TestExempService;

import java.util.List;

@Controller
@RequestMapping("/test_access")
public class TestAccessController {

    private final TestExempService testExempService;
    private final TestAccessGroupDTOService testAccessGroupDTOService;

    public TestAccessController(TestExempService testExempService, TestAccessGroupDTOService testAccessGroupDTOService) {
        this.testExempService = testExempService;
        this.testAccessGroupDTOService = testAccessGroupDTOService;
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
}
