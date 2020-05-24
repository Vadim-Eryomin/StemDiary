package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.LoginRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    RolesRepository rolesRepository;

    String humanId;
    Model model;

    @GetMapping("/home")
    public String home(@CookieValue(defaultValue = "noname") String humanId, Model model){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.model = model;
        this.humanId = humanId;
        //prepare model for this page
        ModelPreparer.prepare(this);
        //if it's good and we found human
        return "home";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public String getHumanId() {
        return humanId;
    }

    public Model getModel() {
        return model;
    }
}
