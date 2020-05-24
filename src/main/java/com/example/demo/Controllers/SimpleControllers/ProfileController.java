package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.Account;
import com.example.demo.Domain.ColorScheme;
import com.example.demo.Domain.Names;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.LoginRepository;
import com.example.demo.Repositories.NamesRepository;
import com.example.demo.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    NamesRepository namesRepository;

    Model model;
    String humanId;

    @GetMapping("/profile")
    public String showProfile(Model model, @CookieValue(defaultValue = "noname") String humanId, @RequestParam(required = false, defaultValue = "") String navColor, @RequestParam(required = false, defaultValue = "") String bodyColor){
        //redirect human if he hasn't login cookie
        if (humanId.equals("noname")) return "redirect:/login";

        //check color edit form
        if (!bodyColor.equals("") && !navColor.equals("")){
            ColorScheme color = colorRepository.findById(Integer.parseInt(humanId)).get(0);
            color.setBodyColor(Integer.parseInt(bodyColor.split("#")[1], 16));
            color.setNavigationColor(Integer.parseInt(navColor.split("#")[1], 16));
            colorRepository.save(color);
        }

        //set data for model preparer
        this.model = model;
        this.humanId = humanId;
        //prepare model for this page
        ModelPreparer.prepare(this);
        return "profile";
    }

    @PostMapping("/profile")
    public String editPassword(Model model, @CookieValue(defaultValue = "noname") String humanId,
                               @RequestParam String oldPassword,
                               @RequestParam String newPassword,
                               @RequestParam String repeatPassword){
        //redirect human if he hasn't login cookie
        if (humanId.equals("noname")) return "redirect:/login";

        if(loginRepository.findById(Integer.parseInt(humanId)).get(0).getPassword().equals(oldPassword)){
            if (newPassword.equals(repeatPassword)){
                Account account = loginRepository.findById(Integer.parseInt(humanId)).get(0).setPassword(newPassword);
                loginRepository.save(account);
                model.addAttribute("success", "Вы сменили пароль!");
            }
            else {
                model.addAttribute("warn", "Пароли не совпадают!");
            }
        }
        else {
            model.addAttribute("warn", "Введите ваш начальный пароль!");
        }

        //set data for model preparer
        this.model = model;
        this.humanId = humanId;
        //prepare model for this page
        ModelPreparer.prepare(this);
        return "profile";
    }



    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public Model getModel() {
        return model;
    }

    public String getHumanId() {
        return humanId;
    }
}
