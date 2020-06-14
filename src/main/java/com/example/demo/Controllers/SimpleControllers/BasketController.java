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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasketController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    UnconfirmedBasketRepository unconfirmedBasketRepository;

    Model model;
    String humanId;

    @GetMapping("/basket")
    public String showBasket(Model model, @CookieValue String humanId) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //prepare model for page
        ModelPreparer.prepare(this);

        return "basket";
    }

    @PostMapping("/basket")
    public String confirmBasket(Model model, @CookieValue String humanId,
                                @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        UnconfirmedBasket unBasket = unconfirmedBasketRepository.findById(id).get(0);
        Basket basket = new Basket();
        basket.setProductId(unBasket.getProductId()).setCustomerId(unBasket.getCustomerId());
        basketRepository.save(basket);

        Status status = new Status();
        status.setId(basket.getId()).setStatus("Не прочитано");
        statusRepository.save(status);
        unconfirmedBasketRepository.delete(unBasket);

        //prepare model for page
        ModelPreparer.prepare(this);

        return "basket";
    }
    @GetMapping("/basketCancel")
    public String cancelBasket(Model model, @CookieValue String humanId,
                               @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        UnconfirmedBasket unBasket = unconfirmedBasketRepository.findById(id).get(0);
        Product product = productRepository.findById(unBasket.getProductId()).get(0);
        product.setCount(product.getCount() + 1);
        productRepository.save(product);
        unconfirmedBasketRepository.delete(unBasket);

        return "redirect:/basket";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public BasketController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public BasketController setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
        return this;
    }

    public StatusRepository getStatusRepository() {
        return statusRepository;
    }

    public BasketController setStatusRepository(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public BasketController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public BasketController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public BasketController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public BasketRepository getBasketRepository() {
        return basketRepository;
    }

    public BasketController setBasketRepository(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
        return this;
    }

    public UnconfirmedBasketRepository getUnconfirmedBasketRepository() {
        return unconfirmedBasketRepository;
    }

    public BasketController setUnconfirmedBasketRepository(UnconfirmedBasketRepository unconfirmedBasketRepository) {
        this.unconfirmedBasketRepository = unconfirmedBasketRepository;
        return this;
    }
}
