package ru.egupov.risktestsystem.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.repositories.PeopleRepository;

@Service
public class PersonService {

    private final PasswordService passwordService;
    private final DefaultEmailService defaultEmailService;

    private final PeopleRepository peopleRepository;

    public PersonService(PasswordService passwordService, DefaultEmailService defaultEmailService, PeopleRepository peopleRepository) {
        this.passwordService = passwordService;
        this.defaultEmailService = defaultEmailService;
        this.peopleRepository = peopleRepository;
    }

    public void registration(Person person){

        try {
            String pass = passwordService.generatePassword();
            person.setPassword(new BCryptPasswordEncoder().encode(pass));
            defaultEmailService.sendSimpleEmail(person.getEmail(), "Доступ в систему тестирования",
                    "Добрый день\nВам предоставлен доступ к системе тестирования. Используйте данные для входа:\n" +
                            "Логин: ваш адрес электронной почты\n" +
                            "Пароль: " + pass + "\n" +
                            "Адрес системы тестирования: http://127.0.0.1:8080");

        } catch (Exception e){
            System.out.println("Не отправлено письмо!" + e.getMessage());
        }

    }

    public void newPass(int id){
        Person person = peopleRepository.getById(id);
        registration(person);
        peopleRepository.save(person);
    }


}
