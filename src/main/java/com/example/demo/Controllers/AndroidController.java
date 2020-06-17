package com.example.demo.Controllers;

import com.example.demo.Domain.*;
import com.example.demo.Domain.JSONDomain.JSONCourse;
import com.example.demo.Domain.JSONDomain.JSONTeacherCourse;
import com.example.demo.Repositories.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class AndroidController {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MarkRepository markRepository;
    @Autowired
    NamesRepository namesRepository;
    @Autowired
    PupilRepository pupilRepository;
    @Autowired
    StemCoinRepository stemCoinRepository;
    @Autowired
    HomeworkRepository homeworkRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    UnconfirmedBasketRepository unconfirmedBasketRepository;
    @Autowired
    StatusRepository statusRepository;


    @PostMapping("/androidLogin")
    public String login(Model model,
                        @RequestParam String login,
                        @RequestParam String password) {
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            Names names = namesRepository.findById(account.getId()).get(0);
            Roles roles = rolesRepository.findById(account.getId()).get(0);

            JsonObject name = new JsonObject();
            name.addProperty("name", names.getName());
            name.addProperty("surname", names.getSurname());
            name.addProperty("admin", roles.isAdmin());
            name.addProperty("teacher", roles.isTeacher());
            name.addProperty("avatarUrl", account.getImgSrc());

            ArrayList<StemCoin> stemCoins = (ArrayList<StemCoin>) stemCoinRepository.findById(account.getId());
            if (stemCoins == null || stemCoins.isEmpty()){
                StemCoin stemCoin = new StemCoin().setId(account.getId()).setStemcoins(0);
                stemCoinRepository.save(stemCoin);
            }
            StemCoin stemCoin = stemCoinRepository.findById(account.getId()).get(0);
            name.addProperty("stemcoin", stemCoin.getStemcoins());

            model.addAttribute("data", name.toString());

        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("/getTeacherCourses")
    public String getTeacherCourses(Model model,
                                    @RequestParam String login,
                                    @RequestParam String password) {
        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            JSONArray array = new JSONArray();
            ArrayList<Course> iAmTeacher = (ArrayList<Course>) courseRepository.findByTeacherId(account.getId());
            iAmTeacher.forEach((course) -> {
                JSONTeacherCourse courseData = new JSONTeacherCourse();
                Names teacher = namesRepository.findById(course.getTeacherId()).get(0);
                ArrayList<Pupil> pupils = pupilRepository.findByCourseId(course.getId());
                ArrayList<Integer> pupilIds = new ArrayList<>();
                ArrayList<String> pupilNames = new ArrayList<>();
                pupils.forEach((pupil) -> {
                    pupilIds.add(pupil.getPupilId());

                    Names pupilName = namesRepository.findById(pupil.getPupilId()).get(0);
                    pupilNames.add(pupilName.getName() + " " + pupilName.getSurname());
                });

                courseData.setPupilId(pupilIds.toArray());
                courseData.setPupilName(pupilNames.toArray());
                courseData.setTeacherId(teacher.getId());
                courseData.setTeacherName(teacher.getName() + " " + teacher.getSurname());
                courseData.setAvatarUrl(course.getImgSrc());
                courseData.setCourseName(course.getName());

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                long date = 0;
                try{
                    while (new Date().after(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()))) {
                        // TODO: 20.05.2020 оптимизировать!!! добавляет неделю, но выглядит жутковато)
                        course.setNextDate(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()).getTime() + 604800000L)));
                    }
                    date = format.parse(course.getNextDate()).getTime();
                }catch (Exception ignore){}


                long preDate = date - 604800000L;
                long postDate = date + 604800000L;

                Homework preHomework = homeworkRepository.findByCourseIdAndDate(course.getId(), preDate).isEmpty() ?
                        new Homework().setDate(preDate).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), preDate).get(0);
                Homework homework = homeworkRepository.findByCourseIdAndDate(course.getId(), date).isEmpty() ?
                        new Homework().setDate(date).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), date).get(0);
                Homework postHomework = homeworkRepository.findByCourseIdAndDate(course.getId(), postDate).isEmpty() ?
                        new Homework().setDate(postDate).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), postDate).get(0);

                homeworkRepository.save(preHomework);
                homeworkRepository.save(homework);
                homeworkRepository.save(postHomework);

                courseData.setPreHomework(preHomework.getHomework());
                courseData.setHomework(homework.getHomework());
                courseData.setPostHomework(postHomework.getHomework());

                courseData.setPreDate(format.format(preDate));
                courseData.setDate(format.format(date));
                courseData.setPostDate(format.format(postDate));

                JSONObject courseObject = new JSONObject();
                courseObject.put("pupilId", courseData.getPupilId());
                courseObject.put("teacherId", courseData.getTeacherId());
                courseObject.put("teacherName", courseData.getTeacherName());
                courseObject.put("teacherAvatarUrl", account.getImgSrc());
                courseObject.put("avatarUrl", courseData.getAvatarUrl());
                courseObject.put("preHomework", courseData.getPreHomework());
                courseObject.put("homework", courseData.getHomework());
                courseObject.put("postHomework", courseData.getPostHomework());
                courseObject.put("preDate", courseData.getPreDate());
                courseObject.put("date", courseData.getDate());
                courseObject.put("postDate", courseData.getPostDate());

                array.put(courseObject);
            });

            model.addAttribute("data", array.toString());
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("/getPupilCourses")
    public String getPupilCourses(Model model,
                                    @RequestParam String login,
                                    @RequestParam String password) {
        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            JSONArray array = new JSONArray();
            ArrayList<Pupil> iAmPupil = pupilRepository.findByPupilId(account.getId());
            iAmPupil.forEach((pupil) -> {
                JSONCourse courseData = new JSONCourse();
                Course course = courseRepository.findById(pupil.getCourseId()).get(0);
                Names teacher = namesRepository.findById(course.getTeacherId()).get(0);
                Account teacherAccount = loginRepository.findById(course.getTeacherId()).get(0);

                courseData.setPupilId(account.getId());
                courseData.setTeacherId(teacher.getId());
                courseData.setTeacherName(teacher.getName() + " " + teacher.getSurname());
                courseData.setAvatarUrl(course.getImgSrc());
                courseData.setCourseName(course.getName());

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                long date = 0;
                try{
                    while (new Date().after(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()))) {
                        // TODO: 20.05.2020 оптимизировать!!! добавляет неделю, но выглядит жутковато)
                        course.setNextDate(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()).getTime() + 604800000L)));
                    }
                    date = format.parse(course.getNextDate()).getTime();
                }catch (Exception ignore){}


                long preDate = date - 604800000L;
                long postDate = date + 604800000L;

                Homework preHomework = homeworkRepository.findByCourseIdAndDate(course.getId(), preDate).isEmpty() ?
                        new Homework().setDate(preDate).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), preDate).get(0);
                Homework homework = homeworkRepository.findByCourseIdAndDate(course.getId(), date).isEmpty() ?
                        new Homework().setDate(date).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), date).get(0);
                Homework postHomework = homeworkRepository.findByCourseIdAndDate(course.getId(), postDate).isEmpty() ?
                        new Homework().setDate(postDate).setHomework("Кажется, ничего нет!").setCourseId(course.getId()) :
                        homeworkRepository.findByCourseIdAndDate(course.getId(), postDate).get(0);

                homeworkRepository.save(preHomework);
                homeworkRepository.save(homework);
                homeworkRepository.save(postHomework);

                courseData.setPreHomework(preHomework.getHomework());
                courseData.setHomework(homework.getHomework());
                courseData.setPostHomework(postHomework.getHomework());

                courseData.setPreDate(format.format(preDate));
                courseData.setDate(format.format(date));
                courseData.setPostDate(format.format(postDate));

                JSONObject courseObject = new JSONObject();
                courseObject.put("pupilId", courseData.getPupilId());
                courseObject.put("teacherId", courseData.getTeacherId());
                courseObject.put("teacherName", courseData.getTeacherName());
                courseObject.put("teacherAvatarUrl", teacherAccount.getImgSrc());
                courseObject.put("avatarUrl", courseData.getAvatarUrl());
                courseObject.put("preHomework", courseData.getPreHomework());
                courseObject.put("homework", courseData.getHomework());
                courseObject.put("postHomework", courseData.getPostHomework());
                courseObject.put("preDate", courseData.getPreDate());
                courseObject.put("date", courseData.getDate());
                courseObject.put("postDate", courseData.getPostDate());
                courseObject.put("courseName", courseData.getCourseName());

                array.put(courseObject);
            });
            //needTime + or - 604800000L
            model.addAttribute("data", array.toString());
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("setStudentRate/{login}/{password}/{courseName}/{date}/{a}/{b}/{c}/{pupilLogin}")
    public String setStudentRate(Model model,
                                 @PathVariable String login,
                                 @PathVariable String password,
                                 @PathVariable String courseName,
                                 @PathVariable String date,
                                 @PathVariable String a,
                                 @PathVariable String b,
                                 @PathVariable String c,
                                 @PathVariable String pupilLogin) throws ParseException {
        int idOfCourse = courseRepository.findByName(courseName).get(0).getId();
        long courseDate = convertDate(date);

        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        Roles roles = rolesRepository.findById(account.getId()).get(0);
        if (roles.isTeacher() || roles.isAdmin()) {
            Mark mark =  markRepository.findByCourseIdAndDateAndPupilId(idOfCourse, courseDate, loginRepository.findByLogin(pupilLogin).get(0).getId()).isEmpty() ?
                    new Mark().setCourseId(idOfCourse).setPupilId(loginRepository.findByLogin(pupilLogin).get(0).getId()).setMarkA(Integer.parseInt(a))
                    .setMarkB(Integer.parseInt(b)).setMarkC(Integer.parseInt(c)).setDate(courseDate).setAdd(false).setTotal((int)((Integer.parseInt(a)+Integer.parseInt(b)+Integer.parseInt(c))/3)):
                    markRepository.findByCourseIdAndDateAndPupilId(idOfCourse, courseDate, loginRepository.findByLogin(login).get(0).getId()).get(0);
            if (!mark.isAdd()) {
                StemCoin stemCoin = stemCoinRepository.findById(mark.getPupilId()).get(0);
                stemCoin.setStemcoins(stemCoin.getStemcoins() + mark.getTotal());
                stemCoinRepository.save(stemCoin);
                model.addAttribute("data", "true");
            }
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("/getAllPupils")
    public String getAllPupils(Model model,
                               @RequestParam String login,
                               @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<Account> accounts = (ArrayList<Account>) loginRepository.findAll();
            accounts.forEach((pupil) -> {
                Roles role = rolesRepository.findById(pupil.getId()).get(0);
                if (role.isTeacher() || role.isAdmin()) return;
                JSONObject pupilObject = new JSONObject();
                Names name = namesRepository.findById(account.getId()).get(0);
                pupilObject.put("name", name.getName());
                pupilObject.put("surname", name.getSurname());
                pupilObject.put("login", account.getLogin());
                array.put(pupilObject);
            });
            model.addAttribute("data", array.toString());
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @GetMapping("/getAllTeachers")
    public String getAllTeachers(Model model,
                               @RequestParam String login,
                               @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<Account> accounts = (ArrayList<Account>) loginRepository.findAll();
            accounts.forEach((pupil) -> {
                Roles role = rolesRepository.findById(pupil.getId()).get(0);
                if (!role.isTeacher()) return;
                JSONObject pupilObject = new JSONObject();
                Names name = namesRepository.findById(account.getId()).get(0);
                pupilObject.put("name", name.getName());
                pupilObject.put("surname", name.getSurname());
                pupilObject.put("login", account.getLogin());
                array.put(pupilObject);
            });
            model.addAttribute("data", array.toString());
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("/getAllShop")
    public String getShop(Model model,
                          @RequestParam String login,
                          @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<Product> products = (ArrayList<Product>) productRepository.findAll();
            products.forEach((product) -> {
                JSONObject productObject = new JSONObject();
                productObject.put("title", product.getTitle());
                productObject.put("count", product.getCount());
                productObject.put("cost", product.getCost());
                productObject.put("avatarUrl", product.getImgSrc());
                productObject.put("about", product.getAbout());
                productObject.put("id", product.getId());
                array.put(productObject);
            });
            model.addAttribute("data", array.toString());
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/getAllConfirmedBaskets")
    public String getAllConfirmedBaskets(Model model,
                          @RequestParam String login,
                          @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<Basket> baskets = (ArrayList<Basket>) basketRepository.findByCustomerId(account.getId());
            baskets.forEach((basket) -> {
                JSONObject basketObject = new JSONObject();
                basketObject.put("id", basket.getId());
                basketObject.put("product", basket.getProductId());
                basketObject.put("productName", productRepository.findById(basket.getProductId()).get(0).getTitle());
                basketObject.put("status", statusRepository.findById(basket.getId()).get(0).getStatus());
                array.put(basketObject);
            });

            model.addAttribute("data", array.toString());
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @GetMapping("/getAllUnconfirmedBaskets")
    public String getAllUnconfirmedBaskets(Model model,
                                         @RequestParam String login,
                                         @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<UnconfirmedBasket> baskets = (ArrayList<UnconfirmedBasket>) unconfirmedBasketRepository.findByCustomerId(account.getId());
            baskets.forEach((basket) -> {
                JSONObject basketObject = new JSONObject();
                basketObject.put("id", basket.getId());
                basketObject.put("product", basket.getProductId());
                basketObject.put("productName", productRepository.findById(basket.getProductId()).get(0).getTitle());
                array.put(basketObject);
            });

            model.addAttribute("data", array.toString());
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/buy")
    public String buy(Model model,
                      @RequestParam String login,
                      @RequestParam String password,
                      @RequestParam int productId){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            Product product = productRepository.findById(productId).get(0);
            if (product.getCount() > 0){
                StemCoin stemCoin = stemCoinRepository.findById(account.getId()).isEmpty() ?
                        new StemCoin().setId(account.getId()).setStemcoins(0) :
                        stemCoinRepository.findById(account.getId()).get(0);
                stemCoinRepository.save(stemCoin);

                if (stemCoin.getStemcoins() >= product.getCost()){
                    stemCoin.setStemcoins(stemCoin.getStemcoins() - product.getCost());
                    stemCoinRepository.save(stemCoin);
                    UnconfirmedBasket basket = new UnconfirmedBasket().setCustomerId(account.getId()).setProductId(product.getId());
                    unconfirmedBasketRepository.save(basket);
                    model.addAttribute("data", "Good");
                }
                else {
                    model.addAttribute("data", "Not enough money!");
                }
            }
            else {
                model.addAttribute("data", "Not enough product!");
            }
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/confirm")
    public String confirmBasket(Model model,
                                @RequestParam String login,
                                @RequestParam String password,
                                @RequestParam int basketId){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            UnconfirmedBasket basket = unconfirmedBasketRepository.findById(basketId).isEmpty() ?
                    null : unconfirmedBasketRepository.findById(basketId).get(0);
            if (basket != null){
                Basket confirmedBasket = new Basket().setCustomerId(basket.getCustomerId()).setProductId(basket.getProductId());
                basketRepository.save(confirmedBasket);

                Status status = new Status().setId(confirmedBasket.getId()).setStatus("Не прочитано");
                statusRepository.save(status);

                unconfirmedBasketRepository.delete(basket);
                model.addAttribute("data", "Good");
            }
            else {
                model.addAttribute("data", "Я хз!");
            }
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/decline")
    public String declineBasket(Model model,
                                @RequestParam String login,
                                @RequestParam String password,
                                @RequestParam int basketId){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            UnconfirmedBasket basket = unconfirmedBasketRepository.findById(basketId).isEmpty() ?
                    null : unconfirmedBasketRepository.findById(basketId).get(0);
            if (basket != null){
                StemCoin stemCoin = stemCoinRepository.findById(account.getId()).get(0);
                stemCoin.setStemcoins(stemCoin.getStemcoins() + productRepository.findById(basket.getProductId()).get(0).getCost());
                stemCoinRepository.save(stemCoin);

                unconfirmedBasketRepository.delete(basket);
                model.addAttribute("data", "Good");
            }
            else {
                model.addAttribute("data", "Я хз!");
            }
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/addCourse")
    public String createCourse(Model model,
                               @RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam long date,
                               @RequestParam String[] pupils,
                               @RequestParam String teacher,
                               @RequestParam String imgSrc){
        if (!loginRepository.findByLoginAndPassword(login, password).isEmpty()){
            Course course = new Course();

            Account teacherAccount = loginRepository.findByLogin(teacher).get(0);
            Names teacherName = namesRepository.findById(teacherAccount.getId()).get(0);

            course.setTeacherId(teacherAccount.getId())
                    .setDate(date)
                    .setImgSrc(imgSrc)
                    .setName(name)
                    .setTeacherName(teacherName.getName() + " " + teacherName.getSurname())
                    .setNextDate(new SimpleDateFormat("dd.MM.yyyy hh:mm").format(new Date(date)));
            courseRepository.save(course);

            for (String pupilLogin :
                    pupils) {
                Pupil pupil = new Pupil();
                pupil.setPupilId(loginRepository.findByLogin(pupilLogin).get(0).getId())
                        .setCourseId(course.getId());
                pupilRepository.save(pupil);
            }
            model.addAttribute("data", "true");
        }
        else {
            model.addAttribute("data", "false");
        }
        return "androidData";
    }


    public static long convertDate(String date) throws ParseException {
        //dd.MM.YYYY HH:mm
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.parse(date).getTime();
    }


}
