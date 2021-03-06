package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.Basket;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.Status;
import com.example.demo.Domain.UnconfirmedBasket;
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
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UnconfirmedBasketRepository unconfirmedBasketRepository;
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
                           @RequestParam(required = false, defaultValue = "999999") int costBelow,
                           @RequestParam(required = false, defaultValue = "0") int buy) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.costAbove = costAbove;
        this.costBelow = costBelow;
        this.title = title;

        ModelPreparer.prepare(this);

        if (buy != 0) {
            UnconfirmedBasket basket = new UnconfirmedBasket();
            basket.setCustomerId(Integer.parseInt(humanId)).setProductId(buy);
            unconfirmedBasketRepository.save(basket);
            Product product = productRepository.findById(basket.getProductId()).get(0);
            product.setCount(product.getCount()-1);
            productRepository.save(product);
        }

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
