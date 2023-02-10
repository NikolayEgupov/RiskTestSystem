package ru.egupov.risktestsystem.services;

import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.repositories.TestExempRepository;

@Service
public class TestExempService {

    private final TestExempRepository testExempRepository;


    public TestExempService(TestExempRepository testExempRepository) {
        this.testExempRepository = testExempRepository;
    }
}
