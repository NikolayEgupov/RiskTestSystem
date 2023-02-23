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
        Date dateStart = convertToDate(testAccessGroupDTO.getDateStart());
        Date dateEnd = convertToDate(testAccessGroupDTO.getDateEnd());
        int timeLimit = testAccessGroupDTO.getTimeLimit();
        Group group = groupService.getById(testAccessGroupDTO.getGroup().getId());
        TestExemp testExemp = testExempService.findById(testAccessGroupDTO.getTestExemp().getId());

        List<Student> students = studentService.findByGroup(group);
        students.forEach(x -> {
            for (int i = 0; i< testAccessGroupDTO.getCountAccess(); i++) {
                TestAccess testAccess = new TestAccess();
                testAccess.setStudent(x);
                testAccess.setTestExemp(testExemp);
                testAccess.setTimeLimit(timeLimit);
                testAccess.setDateStart(dateStart);
                testAccess.setDateEnd(dateEnd);
                testAccessService.save(testAccess);
            }
        });

    }

    private Date convertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate lDate = LocalDate.parse(date, formatter);
        Instant instant = Instant.from(lDate.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }
}
