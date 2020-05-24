package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.LoginRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopController {
    //init all need repositories
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    ColorRepository colorRepository;
    //data for model preparer
    Model model;
    String humanId;
    String title;
    int cost;

    @GetMapping("/shop")
    public String showShop(@CookieValue(defaultValue = "noname") String humanId, Model model, @RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "0") int cost){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        try {
            this.cost = cost;
        }catch (Exception ignore){
            this.cost = 0;
        }
        this.title = title;

        ModelPreparer.prepare(this);

        return "shop";
    }


    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public Model getModel() {
        return model;
    }

    public String getHumanId() {
        return humanId;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }
}
