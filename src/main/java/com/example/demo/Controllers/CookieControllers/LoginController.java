package com.example.demo.Controllers.CookieControllers;

import com.example.demo.Domain.Account;
import com.example.demo.Domain.RegisterRequest;
import com.example.demo.HelpClasses.Cryptographer;
import com.example.demo.Repositories.LoginRepository;
import com.example.demo.Repositories.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class LoginController {
    //import all need repositories
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    RegisterRequestRepository registerRequestRepository;

    // connect to website without login cookie
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //write cookie if connected
    //else -> redirect to login again
    @PostMapping("/login")
    public String checkAndEnter(@RequestParam String login, @RequestParam String password, HttpServletResponse response) {
        //find a human we need and return to login if we can't find him
        ArrayList<Account> possibleHumans = (ArrayList<Account>) loginRepository.findByLoginAndPassword(login, password);
        if (possibleHumans.isEmpty()) return "login";
        else {
            //we found need human
            Account human = possibleHumans.get(0);

            //write 1 hour cookie for next uses
            Cookie cookie = new Cookie("humanId", Cryptographer.code(human.getId() + ""));
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
        }
        //if we found human we give access to our website
        return "redirect:/profile";
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String surname,
                                   @RequestParam String login, @RequestParam String password,
                                   @RequestParam String imgSrc, @RequestParam String email) {
        RegisterRequest request = new RegisterRequest();
        request.setLogin(login).setPassword(password).setName(name).setSurname(surname).setEmail(email).setImgSrc(imgSrc);
        registerRequestRepository.save(request);
        return "allGoodRegister";
    }
}