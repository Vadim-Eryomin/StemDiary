package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.OtherDomain.VKAuthRequest;
import com.example.demo.Domain.OtherDomain.VkUserData;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.RolesRepository;
import com.vk.api.sdk.actions.Auth;
import com.vk.api.sdk.actions.OAuth;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.friends.FriendsGetQuery;
import com.vk.api.sdk.queries.users.UserField;
import com.vk.api.sdk.queries.users.UsersGetQuery;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class FriendsController {
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    RolesRepository rolesRepository;

    Model model;
    String humanId;

    @GetMapping("/getCode")
    public String getCode(@CookieValue(defaultValue = "noname") String humanId, Model model,
                          @RequestParam(required = false, defaultValue = "none") String code) throws ClientException, ApiException {
        this.humanId = humanId;
        this.model = model;
        if (!code.equals("none")){
            //https://oauth.vk.com/authorize?client_id=7512626&display=page&redirect_uri=http://18.191.156.108/getCode&scope=friends&response_type=code&v=5.110
            //https://oauth.vk.com/authorize?client_id=7512626&display=page&redirect_uri=&scope=friends&response_type=code&v=5.110
            //https://oauth.vk.com/access_token?client_id=7512626&client_secret=tZvRCSIitiIJCeCA4MLM&redirect_uri=http://18.191.156.108/getCode&code=450dd7635ee9958248
            HttpTransportClient client = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(client);
            UserAuthResponse res = vk.oauth().userAuthorizationCodeFlow(7512626, "tZvRCSIitiIJCeCA4MLM", "http://18.191.156.108/getCode", code).execute();
            UserActor actor = new UserActor(res.getUserId(), res.getAccessToken());
            ServiceActor service = new ServiceActor(7512626, "tZvRCSIitiIJCeCA4MLM", "32316a2432316a2432316a24e23243c8163323132316a246cd9ce45aaef5ebcf56c7e24");
            List<Integer> friendIds = vk.friends().get(actor).execute().getItems();
            ArrayList<VkUserData> data = new ArrayList<>();
            UsersGetQuery query = vk.users().get(service);
            friendIds.forEach((id) -> {
                try {
                    UserXtrCounters user = query.userIds(id + "").fields(UserField.PHOTO_200).execute().get(0);
                    String url = user.getPhoto200();
                    String name = user.getFirstName() + " " + user.getLastName();
                    VkUserData  userData = new VkUserData().setName(name).setUrl(url);
                    data.add(userData);
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }
            });

            model.addAttribute("data", data);
        }

        ModelPreparer.prepare(this);
        return "code";
    }

    @GetMapping("/getToken")
    public String getToken(@CookieValue(defaultValue = "noname") String humanId, Model model){
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);
        //https://oauth.vk.com/access_token?client_id=7512626&client_secret=tZvRCSIitiIJCeCA4MLM&redirect_uri=http://18.191.156.108/getAccessToken&code=
        return "code";
    }

    @GetMapping("/auth")
    public String auth(@CookieValue(defaultValue = "noname") String humanId, Model model,
                       @RequestParam String token, @RequestParam int id) throws ClientException, ApiException {
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        UserActor actor = new UserActor(id, token);
        HttpTransportClient client = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(client);
        GetResponse friends = vk.friends().get(actor).execute();

        System.out.println(Arrays.toString(friends.getItems().toArray()));
        return "friends";
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public FriendsController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public FriendsController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public FriendsController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public FriendsController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }
}
