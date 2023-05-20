package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Question;
import ru.egupov.risktestsystem.repositories.QuestionsRepository;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionsRepository questionsRepository;
    private final TestExempService testExempService;

    public QuestionService(QuestionsRepository questionsRepository, TestExempService testExempService) {
        this.questionsRepository = questionsRepository;
        this.testExempService = testExempService;
    }

    public List<Question> findBYTestExempId (int testExempId){
        return questionsRepository.findByTestExemp(testExempService.findById(testExempId));
    }

    public Question findById(int id){
        return questionsRepository.findById(id).orElse(null);
    }

    public void update(int id, Question question){
        question.setId(id);
        save(question);
    }

    public void save(Question question){
        questionsRepository.save(question);
    }

    public void delete(int id){
        questionsRepository.deleteById(id);
    }
}
