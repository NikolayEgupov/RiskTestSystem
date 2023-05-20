package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Attempt;
import ru.egupov.risktestsystem.models.TestAccess;
import ru.egupov.risktestsystem.repositories.AttemptRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final TestAccessService testAccessService;

    public AttemptService(AttemptRepository attemptRepository, TestAccessService testAccessService) {
        this.attemptRepository = attemptRepository;
        this.testAccessService = testAccessService;
    }

    public int checkOpenAccess(int id){
        TestAccess testAccess = testAccessService.findById(id);

        if (testAccess.getCountAccess() - testAccess.getCountUse() < 1)
            return -2;

        if (testAccess.getDateStart().after(new Date()) ||
            testAccess.getDateEnd().before(new Date()))
            return -1;

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
            return -2;

        if (attemptUse != null)
            return 2;
        else
            return 1;

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


