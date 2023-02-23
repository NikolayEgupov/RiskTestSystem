package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.DTO.TestAccessGroupDTO;
import ru.egupov.risktestsystem.DTO.TestAccessStudentDTO;
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
public class TestAccessStudentDTOService {

    private final TestExempService testExempService;
    private final StudentService studentService;
    private final TestAccessService testAccessService;



    public TestAccessStudentDTOService(TestExempService testExempService, StudentService studentService, TestAccessService testAccessService) {
        this.testExempService = testExempService;
        this.studentService = studentService;
        this.testAccessService = testAccessService;
    }


    public void addAccess(TestAccessStudentDTO testAccessStudentDTO){
        List<TestAccess> testAccesses = convertToListTestAcess(testAccessStudentDTO);
        testAccesses.forEach(testAccessService::save);
    }

    private List<TestAccess> convertToListTestAcess(TestAccessStudentDTO testAccessStudentDTO){

        List<TestAccess> testAccessList = new ArrayList<>();

        Date dateStart = convertToDate(testAccessStudentDTO.getDateStart());
        Date dateEnd = convertToDate(testAccessStudentDTO.getDateEnd());
        int timeLimit = testAccessStudentDTO.getTimeLimit();
        int countAccess = testAccessStudentDTO.getCountAccess();
        TestExemp testExemp = testExempService.findById(testAccessStudentDTO.getTestExemp().getId());
        Student student = studentService.findById(testAccessStudentDTO.getStudent().getId());

        TestAccess testAccess = new TestAccess();
        testAccess.setStudent(student);
        testAccess.setTestExemp(testExemp);
        testAccess.setTimeLimit(timeLimit);
        testAccess.setDateStart(dateStart);
        testAccess.setDateEnd(dateEnd);
        testAccess.setCountAccess(countAccess);
        testAccessList.add(testAccess);

        return testAccessList;
    }

    private Date convertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate lDate = LocalDate.parse(date, formatter);
        Instant instant = Instant.from(lDate.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }
}
