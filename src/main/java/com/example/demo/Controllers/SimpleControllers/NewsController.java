package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.ModelDomain.Post;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.ColorRepository;
import com.example.demo.Repositories.RolesRepository;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class NewsController {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ColorRepository colorRepository;

    String humanId;
    Model model;
    String path = "image/noroot.jpg";


    @GetMapping("/news")
    public String showNews(@CookieValue(defaultValue = "noname") String humanId, Model model) throws ClientException, ApiException {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;

        //prepare model for page
        ModelPreparer.prepare(this);

        //get service from VK
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        ServiceActor actor = new ServiceActor(7193860, "tqCMcR6AEVmEfAAeIx3o", "723d3fac723d3fac723d3fac277250faa87723d723d3fac2f83444d86eda24ee779618a");

        //get all posts from the wall
        GetResponse wall = vk.wall().get(actor).ownerId(-113376999).execute();
        ArrayList<Post> posts = new ArrayList<>();
        int index = 1;
        //rebuild all news for our format
        for (WallPostFull wallpost : wall.getItems()) {
            Post post = new Post();
            //restructuring text
            String[] words = wallpost.getText().split(" ");
            int length = 0;
            String text = "";

            for (int i = 0; i < words.length - 1; i++) {
                try {
                    text += words[i] + " ";
                    length += words[i].length();
                    if (length >= 300) {
                        text += "...";
                        break;
                    }
                } catch (Exception ignore) {
                }
            }

            //get date from post and format it to need text
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date(wallpost.getDate() * 1000L);
            String dateText = format.format(date);

            //get attachments from post and get photo or preview from video else use stem-logo
            String url = "";
            if (wallpost.getAttachments() != null) {
                for (WallpostAttachment attachment : wallpost.getAttachments()) {
                    try {
                        if (attachment.getType() == WallpostAttachmentType.PHOTO) {
                            url = attachment.getPhoto().getPhoto807();
                            break;
                        } else if (attachment.getType() == WallpostAttachmentType.VIDEO) {
                            url = attachment.getVideo().getPhoto800();
                            break;
                        } else {
                            url = path;
                        }
                    } catch (Exception ignore) {
                        url = path;
                    }
                }
            } else {
                url = path;
            }


            //get post's url
            String postUrl = "https://vk.com/coistem?w=wall-113376999_" + wallpost.getId();

            //set all need data
            post.setId(index++);
            post.setUrlToPost(postUrl);
            post.setUrlToImage(url);
            post.setText(text);
            post.setDate(dateText);

            //add post to list
            posts.add(post);
        }
        model.addAttribute("posts", posts);
        return "news";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public String getHumanId() {
        return humanId;
    }

    public Model getModel() {
        return model;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }
}
