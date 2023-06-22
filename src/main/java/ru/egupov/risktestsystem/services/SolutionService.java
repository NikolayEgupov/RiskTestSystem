package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Attempt;
import ru.egupov.risktestsystem.models.Question;
import ru.egupov.risktestsystem.models.Solution;
import ru.egupov.risktestsystem.repositories.SolutionRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public SolutionService(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    public Solution findById(int id){
        return solutionRepository.findById(id).orElse(null);
    }

    public void save(Solution solution){
        solutionRepository.save(solution);
    }

    public List<Solution> generate(Attempt attempt){
        List<Solution> solutions = new ArrayList<>();
        int count = 1;
        for (Question question: attempt.getTestAccess().getTestExemp().getQuestions()) {
            Solution solution = new Solution();
            solution.setAttempt(attempt);
            solution.setQuestion(question);
            solution.setPriority(count);
            save(solution);
            count++;
        }
        attempt.setSolutions(solutions);
        return solutions;
    }

    public Solution getNext(Solution solution){
        List<Solution> solutions = solution.getAttempt().getSolutions()
                .stream().sorted(Comparator.comparing(Solution::getPriority)).collect(Collectors.toList());
        int indexNext = solutions.indexOf(solution) + 1;
        if (indexNext == solutions.size()){
            indexNext = 0;
        }
        return indexNext == 0 ? null : solutions.get(indexNext);
    }
}
