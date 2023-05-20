package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Student;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.models.TestExemp;
import ru.egupov.risktestsystem.repositories.TestAccessRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestAccessService {

    private final TestAccessRepository testAccessRepository;
    private final DefaultEmailService defaultEmailService;

    public TestAccessService(TestAccessRepository testAccessRepository, DefaultEmailService defaultEmailService) {
        this.testAccessRepository = testAccessRepository;
        this.defaultEmailService = defaultEmailService;
    }

    public List<TestAccess> findByTestExempAndDateUseIsNull(TestExemp testExemp){
        return testAccessRepository.findByTestExempForUse(testExemp);
    }

    public TestAccess findById(int id){
        return testAccessRepository.findById(id).orElse(null);
    }

    public List<TestAccess> findActualByStudent(Student student){
        List<TestAccess> testAccesses = testAccessRepository.findByStudentActual(student);
        Date date = new Date();
        return testAccesses.stream().filter(x->x.getDateEnd().after(date)&&x.getDateStart().before(date)).collect(Collectors.toList());
    }

    public void save(TestAccess testAccess){
        testAccessRepository.save(testAccess);
        newMessage(testAccess);
    }

    public void newMessage(TestAccess testAccess){
        defaultEmailService.sendSimpleEmail(testAccess.getStudent().getEmail(), "Назначено тестирование",
                "Добрый день\nВам открыт доступ к материалу тестирования." +
                        "\nНазвание материала: " + testAccess.getTestExemp().getName() +
                        "\nДата открытия доступа: " + testAccess.getDateStart() +
                        "\nДата закрытия доступа: " + testAccess.getDateEnd() +
                        "\nВремя прохождения: " + testAccess.getTimeLimit() +
                        "\nКоличество попыток: " + testAccess.getCountAccess() +
                        "\nИспользуйте данные для входа:" +
                        "\nЛогин: ваш адрес электронной почты" +
                        "\nПароль: отправлен ранее" +
                        "\nАдрес системы тестирования: http://127.0.0.1:8080");
    }
}
