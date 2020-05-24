package com.example.demo.Controllers.AdminControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainAdminPanelController {
    @Autowired
    ColorRepository colorRepository;

    @Autowired
    RolesRepository rolesRepository;

    String humanId;
    Model model;

    @GetMapping("/admin")
    public String showAdminPanel(Model model, @CookieValue(defaultValue = "noname") String humanId){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminPanel";
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainAdminPanelController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainAdminPanelController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainAdminPanelController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainAdminPanelController setModel(Model model) {
        this.model = model;
        return this;
    }
}
