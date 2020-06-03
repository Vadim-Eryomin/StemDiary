package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.LoginRepository;
import com.example.demo.Repositories.NamesRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    NamesRepository namesRepository;

    Model model;
    String humanId;

    @RequestMapping("/error")
    public String checkError(@CookieValue(defaultValue = "noname") String humanId, Model model){
        //human wants to login or this human hasn't an account
        if (humanId.equals("noname")){
            return "redirect:/login";
        }
        //else: this is server error or human is stupid
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);
        return "error";
    }

    @RequestMapping("/")
    public String nullPage(){
        //human wants to login or this human hasn't an account
        if (humanId.equals("noname")){
            return "redirect:/login";
        }
        else {
            return "redirect:/profile";
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public void setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public void setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public void setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(String humanId) {
        this.humanId = humanId;
    }
}
