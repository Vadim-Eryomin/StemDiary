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

import java.util.ArrayList;

@Controller
public class MainAccountsAdminController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    NamesRepository namesRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    PupilRepository pupilRepository;

    Model model;
    String humanId;
    int id;

    @GetMapping("/adminAccount")
    public String showTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminAccount";
    }

    @GetMapping("/adminAccountCreate")
    public String showAccountCreation(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //different preparer for account creation
        ModelPreparer.prepareAccountCreation(this);

        return "accountCreate";
    }

    @PostMapping("/adminAccountCreate")
    public String createAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                @RequestParam String name, @RequestParam String surname,
                                @RequestParam(defaultValue = "false", required = false) boolean isAdmin, @RequestParam(defaultValue = "false", required = false) boolean isTeacher,
                                @RequestParam String login, @RequestParam String password,
                                @RequestParam String imgSrc) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //save data for login
        Account account = new Account();
        account.setLogin(login).setPassword(password);
        loginRepository.save(account);

        //save role data
        Roles roles = new Roles();
        System.out.println(isAdmin);
        System.out.println(isTeacher);
        roles.setId(account.getId()).setAdmin(isAdmin).setTeacher(isTeacher);
        rolesRepository.save(roles);

        //save name and surname
        Names names = new Names();
        names.setId(account.getId()).setName(name).setSurname(surname);
        namesRepository.save(names);

        //save default color scheme
        ColorScheme colorScheme = new ColorScheme();
        colorScheme.setId(account.getId()).setNavigationColor(Integer.parseInt("ffffff", 16)).setBodyColor(Integer.parseInt("ffffff", 16));
        colorRepository.save(colorScheme);

        //prepare model for next page
        ModelPreparer.prepare(this);

        //we saved need info and redirect to main page
        return "redirect:/adminAccount";
    }

    @GetMapping("/adminAccountEdit")
    public String showAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        //different preparer for account edition
        ModelPreparer.prepareAccountEdition(this);
        return "accountEdit";
    }

    @PostMapping("/adminAccountEdit")
    public String editAccount(@CookieValue(defaultValue = "noname") String humanId, Model model,
                              @RequestParam int id, @RequestParam(defaultValue = "noname") String name,
                              @RequestParam(defaultValue = "noname") String surname,
                              @RequestParam(required = false, defaultValue = "false") boolean isAdmin,
                              @RequestParam(required = false, defaultValue = "false") boolean isTeacher,
                              @RequestParam(defaultValue = "noname") String login,
                              @RequestParam(defaultValue = "noname") String password) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        if (name.equals("noname") || surname.equals("noname") ||
                login.equals("noname") || password.equals("noname")) {
            model.addAttribute("warn", "Пожалуйста, заполните все поля!");
            return "redirect:/accountEdit";
        } else {
            //save data for login
            Account account = loginRepository.findById(id).get(0);
            account.setLogin(login).setPassword(password);
            loginRepository.save(account);

            //save role data
            Roles roles = rolesRepository.findById(id).get(0);
            roles.setId(account.getId()).setAdmin(isAdmin).setTeacher(isTeacher);
            rolesRepository.save(roles);

            //save name and surname
            Names names = namesRepository.findById(id).get(0);
            names.setId(account.getId()).setName(name).setSurname(surname);
            namesRepository.save(names);
        }

        //prepare model for next page
        ModelPreparer.prepare(this);
        //we saved need info and redirect to main page
        return "redirect:/adminAccount";
    }

    @GetMapping("/adminAccountDelete")
    public String deleteTimetable(@RequestParam int id, @CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        //prepare model for page
        ModelPreparer.prepare(this);

        Account account = loginRepository.findById(id).get(0);
        Roles roles = rolesRepository.findById(id).get(0);
        Names names = namesRepository.findById(id).get(0);

        if (roles.isTeacher()) {
            //if human is teacher then delete all his courses and pupils
            ArrayList<Course> courses = (ArrayList<Course>) courseRepository.findByTeacherId(names.getId());
            ArrayList<Pupil> pupils = new ArrayList<>();
            for (Course c:
                 courses) {
                pupils.addAll(pupilRepository.findByCourseId(c.getId()));
                courseRepository.delete(c);
            }
            for (Pupil p :
                    pupils) {
                pupilRepository.delete(p);
            }
        }

        namesRepository.delete(names);
        rolesRepository.delete(roles);
        loginRepository.delete(account);

        return "redirect:/adminAccount";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainAccountsAdminController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainAccountsAdminController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public MainAccountsAdminController setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainAccountsAdminController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainAccountsAdminController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public MainAccountsAdminController setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        return this;
    }

    public int getId() {
        return id;
    }

    public MainAccountsAdminController setId(int id) {
        this.id = id;
        return this;
    }
}
