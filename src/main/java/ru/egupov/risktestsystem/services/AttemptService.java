package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.*;
import ru.egupov.risktestsystem.repositories.AttemptRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final TestAccessService testAccessService;

    private final SolutionService solutionService;

    public AttemptService(AttemptRepository attemptRepository, TestAccessService testAccessService, SolutionService solutionService) {
        this.attemptRepository = attemptRepository;
        this.testAccessService = testAccessService;
        this.solutionService = solutionService;
    }

    public Attempt findById(int id){
        return attemptRepository.findById(id).orElse(null);
    }

    public List<Attempt> findAllArchive(Student student){
        return attemptRepository.findByTestAccessStudentAndAndDateEndIsNotNull(student);
    }
    public List<Attempt> findAllArchiveByTest(TestExemp testExemp){
        return attemptRepository.findByTestAccessTestExempAndDateEndIsNotNull(testExemp);
    }

    public int checkOpenAccess(int id){
        //Получение информации о доступе к материалу теста
        TestAccess testAccess = testAccessService.findById(id);

        //Проверка на использование всех доступных попыток
        if (testAccess.getCountAccess() - testAccess.getCountUse() < 1)
            return -2;

        //Проверка даты прохождения теста - не ранее даты начала
        //и не позже даты окончания возможного прохождения тестирования
        if (testAccess.getDateStart().after(new Date()) ||
            testAccess.getDateEnd().before(new Date()))
            return -1;

        //Получение всех попыток по доступу к материалу тестирования
        List<Attempt> attempts = attemptRepository.findByTestAccess(testAccess);

        //Закрытие всех попыток, если время прохождения уже вышло
        refreshAttempts(attempts);

        //Проверка на открытые попытки - исключение ситуации, когда студент открывает новую попытку,
        //не завершив предыдущую
        int countUse = 0;
        Attempt attemptUse = null;
        for (Attempt attempt: attempts) {
            if (attempt.getDateEnd() != null)
                countUse+=1;
            else
                attemptUse = attempt;
        }

        //Повторные проверки
        if (testAccess.getCountAccess() - countUse < 1)
            return -2;

        if (attemptUse != null)
            return 2;
        else
            return 1;

    }

    public int checkProcessAttempt(Attempt attempt){
        int result;
        TestAccess testAccess = attempt.getTestAccess();
        Date dtEnd = new Date(attempt.getDateStart().getTime() + (long) testAccess.getTimeLimit() *60*1000);
        if (attempt.getDateEnd() != null || new Date().after(dtEnd)){
            closeAttempt(attempt, dtEnd);
            return -1;
        }else {
            return 1;
        }
    }

    public Attempt openAccess(TestAccess testAccess){
        if (testAccess.getCountAccess() - testAccess.getCountUse() < 1)
            return null;

        if (testAccess.getDateStart().after(new Date()) ||
                testAccess.getDateEnd().before(new Date()))
            return null;

        List<Attempt> attempts = attemptRepository.findByTestAccess(testAccess);
        refreshAttempts(attempts);

        int countUse = 0;
        Attempt attemptUse = null;
        for (Attempt attempt: attempts) {
            if (attempt.getDateEnd() != null)
                countUse+=1;
            else
                attemptUse = attempt;
        }

        if (testAccess.getCountAccess() - countUse < 1)
            return null;

        if (attemptUse == null)
            attemptUse = newAttempt(testAccess);

        return attemptUse;
    }

    public Attempt newAttempt(TestAccess testAccess){
        Attempt attempt = new Attempt();
        attempt.setTestAccess(testAccess);
        attempt.setDateStart(new Date());
        save(attempt);
        solutionService.generate(attempt);
        return attempt;
    }

    public void closeAttempt(Attempt attempt, Date dateEnd){
        attempt.setDateEnd(dateEnd);
        TestAccess testAccess = attempt.getTestAccess();
        testAccess.setCountUse(testAccess.getCountUse()+1);
        testAccessService.save(testAccess);
        save(attempt);
        calculate(attempt);
    }

    public void calculate(Attempt attempt){
        float maxBall = 0;
        for (Solution solution: attempt.getSolutions()) {
            maxBall += solution.getCountBall();
            System.out.println("solution.getCountBall() " + solution.getCountBall());
        }
        System.out.println("maxBall " + maxBall);
        attempt.setResult(maxBall);
        save(attempt);
    }

    public void refreshAttempts(List<Attempt> attempts) {
        Date date = new Date();
        for (Attempt attempt : attempts) {
            TestAccess testAccess = attempt.getTestAccess();
            if (attempt.getDateEnd() == null) {
                long diff = date.getTime() - attempt.getDateStart().getTime();
                if (TimeUnit.MILLISECONDS.toMinutes(diff) > testAccess.getTimeLimit()) {
                    attempt.setDateEnd(date);
                    save(attempt);
                    testAccess.setCountUse(testAccess.getCountUse() + 1);
                    testAccessService.save(testAccess);
                }
            }
        }
    }

    public void save(Attempt attempt){
        attemptRepository.save(attempt);
    }
}


