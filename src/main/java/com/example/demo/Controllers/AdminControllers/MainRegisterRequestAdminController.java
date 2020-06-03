package com.example.demo.Controllers.AdminControllers;

import com.example.demo.Domain.*;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
public class MainRegisterRequestAdminController {
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    NamesRepository namesRepository;
    @Autowired
    RegisterRequestRepository registerRequestRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    ArchiveRegisterRequestRepository archiveRegisterRequestRepository;

    String humanId;
    Model model;
    int id;

    static String to = "";
    static String from = "vadimeryomin22@gmail.com";
    static String host = "smtp.gmail.com";

    @GetMapping("/adminRequests")
    public String requests(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminRequests";
    }

    @GetMapping("/adminRequestEdit")
    public String showRequest(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        ModelPreparer.prepareRequestEditing(this);

        return "requestEdit";
    }

    @PostMapping("adminRequestEdit")
    public String saveRequest(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id, @RequestParam String name,
                              @RequestParam String surname, @RequestParam String login,
                              @RequestParam String password, @RequestParam String imgSrc,
                              @RequestParam String email, @RequestParam(required = false, defaultValue = "false") boolean isAdmin,
                              @RequestParam(required = false, defaultValue = "false") boolean isTeacher,
                              @RequestParam String phone, @RequestParam String course) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        Account account = new Account();
        account.setLogin(login).setPassword(password).setImgSrc(imgSrc).setEmail(email);
        loginRepository.save(account);

        Roles roles = new Roles();
        roles.setAdmin(isAdmin).setTeacher(isTeacher).setId(account.getId());
        rolesRepository.save(roles);

        Names names = new Names();
        names.setId(account.getId()).setName(name).setSurname(surname);
        namesRepository.save(names);

        ArchiveRegisterRequest archive = new ArchiveRegisterRequest();
        archive.setId(id).setCourse(course).setEmail(email).setImgSrc(imgSrc).setLogin(login).setName(name)
                .setPassword(password).setPhone(phone).setSurname(surname).setAllowed(true);
        archiveRegisterRequestRepository.save(archive);

        registerRequestRepository.delete(registerRequestRepository.findById(id).get(0));
        return "redirect:/adminRequests";
    }

    @GetMapping("/adminRequestClaim")
    public String claimRequest(@CookieValue(defaultValue = "noname") String humanId, Model model,
                               @RequestParam int id) {
        RegisterRequest rr = registerRequestRepository.findById(id).get(0);
        return saveRequest(humanId, model, id, rr.getName(), rr.getSurname(), rr.getLogin(), rr.getPassword(), rr.getImgSrc(),
                rr.getEmail(), false, false, rr.getPhone(), rr.getCourse());
    }

    @GetMapping("/adminRequestDelete")
    public String deleteRequest(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        ArchiveRegisterRequest archive = new ArchiveRegisterRequest();
        RegisterRequest request = registerRequestRepository.findById(id).get(0);

        archive.setId(request.getId()).setName(request.getName()).setSurname(request.getSurname()).setPhone(request.getPhone())
                .setPassword(request.getPassword()).setLogin(request.getLogin()).setImgSrc(request.getImgSrc()).setEmail(request.getEmail())
                .setCourse(request.getCourse()).setAllowed(false);
        archiveRegisterRequestRepository.save(archive);

        registerRequestRepository.delete(registerRequestRepository.findById(id).get(0));

        return "redirect:/adminRequests";
    }


    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainRegisterRequestAdminController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public MainRegisterRequestAdminController setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
        return this;
    }

    public RegisterRequestRepository getRegisterRequestRepository() {
        return registerRequestRepository;
    }

    public MainRegisterRequestAdminController setRegisterRequestRepository(RegisterRequestRepository registerRequestRepository) {
        this.registerRequestRepository = registerRequestRepository;
        return this;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public MainRegisterRequestAdminController setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainRegisterRequestAdminController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainRegisterRequestAdminController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainRegisterRequestAdminController setModel(Model model) {
        this.model = model;
        return this;
    }

    public int getId() {
        return id;
    }

    public MainRegisterRequestAdminController setId(int id) {
        this.id = id;
        return this;
    }
}