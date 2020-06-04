package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.*;
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
    @Autowired
    StemCoinRepository stemCoinRepository;
    //data for model preparer
    Model model;
    String humanId;
    String title;
    int costAbove;
    int costBelow;

    @GetMapping("/shop")
    public String showShop(@CookieValue(defaultValue = "noname") String humanId, Model model,
                           @RequestParam(required = false, defaultValue = "") String title,
                           @RequestParam(required = false, defaultValue = "0") int costAbove,
                           @RequestParam(required = false, defaultValue = "999999") int costBelow){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        System.out.println("redirect isn't active");
        //set data for model preparer
        System.out.println("start model data preparing");
        this.humanId = humanId;
        this.model = model;
        this.costAbove = costAbove;
        this.costBelow = costBelow;
        this.title = title;
        System.out.println("end model data preparing");
        System.out.println("start preparing");
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

    public ShopController setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        return this;
    }

    public ShopController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public ShopController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public ShopController setModel(Model model) {
        this.model = model;
        return this;
    }

    public ShopController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public ShopController setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getCostAbove() {
        return costAbove;
    }

    public ShopController setCostAbove(int costAbove) {
        this.costAbove = costAbove;
        return this;
    }

    public int getCostBelow() {
        return costBelow;
    }

    public ShopController setCostBelow(int costBelow) {
        this.costBelow = costBelow;
        return this;
    }

    public StemCoinRepository getStemCoinRepository() {
        return stemCoinRepository;
    }

    public ShopController setStemCoinRepository(StemCoinRepository stemCoinRepository) {
        this.stemCoinRepository = stemCoinRepository;
        return this;
    }
}
