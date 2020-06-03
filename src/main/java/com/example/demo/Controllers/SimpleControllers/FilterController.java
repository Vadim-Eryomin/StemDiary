package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FilterController {
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    RolesRepository rolesRepository;

    String humanId;
    Model model;

    @GetMapping("/filter")
    public String filter(Model model, @CookieValue(defaultValue = "noname") String humanId){

        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);
        return "filter";
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public FilterController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public FilterController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public FilterController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public FilterController setModel(Model model) {
        this.model = model;
        return this;
    }
}
