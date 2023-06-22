package ru.egupov.risktestsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.repositories.PeopleRepository;
import ru.egupov.risktestsystem.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByEmail(s);

        if (!person.isPresent())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }
}
