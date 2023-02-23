package ru.egupov.risktestsystem.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.egupov.risktestsystem.models.Group;
import ru.egupov.risktestsystem.models.TestExemp;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TestAccessGroupDTO {

    TestExemp testExemp;

    Group group;

    int timeLimit;

    String dateStart;

    String dateEnd;

    int actOption;

    int countAccess;
}
