package com.example.demo.Controllers.AdminControllers;

import com.example.demo.Domain.Product;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainShopAdminController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ColorRepository colorRepository;


    Model model;
    String humanId;
    int id;

    @GetMapping("/adminShop")
    public String showTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminShop";
    }

    @GetMapping("/adminShopCreate")
    public String showAccountCreation(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //different preparer for shop product creation
        ModelPreparer.prepareShopCreation(this);

        return "shopCreate";
    }

    @PostMapping("/adminShopCreate")
    public String createAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                @RequestParam String title, @RequestParam int cost,
                                @RequestParam String srcToImg, @RequestParam String about,
                                @RequestParam(required = false, defaultValue = "0") int count) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //set data for new product and save
        Product product = new Product();
        product.setTitle(title).setAbout(about).setCost(cost).setImgSrc(srcToImg).setCount(count);
        productRepository.save(product);

        //prepare model for next page
        ModelPreparer.prepare(this);

        //we saved need info and redirect to main page
        return "redirect:/adminShop";
    }

    @GetMapping("/adminShopEdit")
    public String showAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        //different preparer for account edition
        ModelPreparer.prepareShopEdition(this);
        return "shopEdit";
    }

    @PostMapping("/adminShopEdit")
    public String editAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id,
                              @RequestParam String title, @RequestParam int cost,
                              @RequestParam String srcToImg, @RequestParam String about,
                              @RequestParam(required = false, defaultValue = "0") int count) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        //find and edit product
        Product product = productRepository.findById(id).get(0);
        product.setTitle(title).setAbout(about).setImgSrc(srcToImg).setCost(cost).setCount(count);
        productRepository.save(product);

        //prepare model for next page
        ModelPreparer.prepare(this);
        //we saved need info and redirect to main page
        return "redirect:/adminShop";
    }

    @GetMapping("/adminShopDelete")
    public String deleteTimetable(@RequestParam int id, @CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        //prepare model for page
        ModelPreparer.prepare(this);

        //delete need product
        Product product = productRepository.findById(id).get(0);
        productRepository.delete(product);

        return "redirect:/adminShop";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainShopAdminController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public MainShopAdminController setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainShopAdminController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainShopAdminController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainShopAdminController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public int getId() {
        return id;
    }

    public MainShopAdminController setId(int id) {
        this.id = id;
        return this;
    }


}
