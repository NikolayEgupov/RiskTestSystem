package ru.egupov.risktestsystem.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.egupov.risktestsystem.models.Person;
import ru.egupov.risktestsystem.security.PersonDetails;
import ru.egupov.risktestsystem.security.SysRole;

@Controller
@RequestMapping("/")
public class CommonController {

    @GetMapping
    public String mainPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String returnPage = "main";

        if (!(authentication instanceof AnonymousAuthenticationToken)){

            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            Person person = personDetails.getPerson();
            if (person.getRole() == SysRole.ROLE_ADMIN){
                returnPage = "redirect:/admin";
            } else if (person.getRole() == SysRole.ROLE_TEACHER){
                returnPage = "teacher_pages/main";
            }
        }

        return returnPage;
    }
}
