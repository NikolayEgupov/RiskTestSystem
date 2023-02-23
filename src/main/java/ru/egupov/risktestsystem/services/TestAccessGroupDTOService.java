package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.DTO.TestAccessGroupDTO;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class TestAccessGroupDTOService {

    private final GroupService groupService;
    private final TestExempService testExempService;
    private final StudentService studentService;
    private final TestAccessService testAccessService;



    public TestAccessGroupDTOService(GroupService groupService, TestExempService testExempService, StudentService studentService, TestAccessService testAccessService) {
        this.groupService = groupService;
        this.testExempService = testExempService;
        this.studentService = studentService;
        this.testAccessService = testAccessService;
    }


    public void addAccess(TestAccessGroupDTO testAccessGroupDTO){
        List<TestAccess> testAccesses = convertToListTestAcess(testAccessGroupDTO);
        testAccesses.forEach(testAccessService::save);
    }

    private List<TestAccess> convertToListTestAcess(TestAccessGroupDTO testAccessGroupDTO){

        List<TestAccess> testAccessList = new ArrayList<>();

        Date dateStart = convertToDate(testAccessGroupDTO.getDateStart());
        Date dateEnd = convertToDate(testAccessGroupDTO.getDateEnd());
        int timeLimit = testAccessGroupDTO.getTimeLimit();
        int countAccess = testAccessGroupDTO.getCountAccess();
        Group group = groupService.getById(testAccessGroupDTO.getGroup().getId());
        TestExemp testExemp = testExempService.findById(testAccessGroupDTO.getTestExemp().getId());

        List<Student> students = studentService.findByGroup(group);
        students.forEach(x -> {
            TestAccess testAccess = new TestAccess();
            testAccess.setStudent(x);
            testAccess.setTestExemp(testExemp);
            testAccess.setTimeLimit(timeLimit);
            testAccess.setDateStart(dateStart);
            testAccess.setDateEnd(dateEnd);
            testAccess.setCountAccess(countAccess);
            testAccessList.add(testAccess);
        });

        return testAccessList;
    }

    private Date convertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate lDate = LocalDate.parse(date, formatter);
        Instant instant = Instant.from(lDate.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }
}
