package ru.egupov.risktestsystem.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestExemp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TestAccessStudentDTO {

    TestExemp testExemp;

    Student student;

    int timeLimit;

    String dateStart;

    String dateEnd;

    int actOption;

    int countAccess;

}
