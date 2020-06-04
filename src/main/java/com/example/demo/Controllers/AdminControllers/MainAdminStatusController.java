package com.example.demo.Controllers.AdminControllers;

import com.example.demo.Domain.Basket;
import com.example.demo.Domain.Status;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainAdminStatusController {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    NamesRepository namesRepository;
    @Autowired
    ProductRepository productRepository;


    Model model;
    String humanId;
    int id;

    @GetMapping("/adminStatus")
    public String showStatuses(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminStatus";
    }

    @GetMapping("/adminStatusEdit")
    public String showStatusEditing(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                    @RequestParam int id){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        Basket basket = basketRepository.findById(id).get(0);
        Status status = statusRepository.findById(basket.getId()).get(0);
        status.setStatus("Прочитан");
        statusRepository.save(status);

        ModelPreparer.prepareStatusEditing(this);
        return "statusEdit";
    }
    @PostMapping("/adminStatusEdit")
    public String editStatus(@CookieValue(defaultValue = "noname") String humanId, Model model,
                             @RequestParam int id, @RequestParam String status){

        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        Status productStatus = statusRepository.findById(id).get(0);
        productStatus.setStatus(status);
        statusRepository.save(productStatus);

        ModelPreparer.prepare(this);
        return "redirect:/adminStatus";
    }


    public StatusRepository getStatusRepository() {
        return statusRepository;
    }

    public MainAdminStatusController setStatusRepository(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
        return this;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainAdminStatusController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainAdminStatusController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public BasketRepository getBasketRepository() {
        return basketRepository;
    }

    public MainAdminStatusController setBasketRepository(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
        return this;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public MainAdminStatusController setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainAdminStatusController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainAdminStatusController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public int getId() {
        return id;
    }

    public MainAdminStatusController setId(int id) {
        this.id = id;
        return this;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public MainAdminStatusController setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        return this;
    }
}
