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


import javax.management.relation.Role;
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
                courseObject.put("courseName", course.getName());
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

                long finalDate = date;
                JSONArray pupilsArray = new JSONArray();
                pupils.forEach(pupil -> {
                    Mark preMark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), preDate, pupil.getPupilId()) ?
                            markRepository.findByCourseIdAndDateAndPupilId(course.getId(), preDate, pupil.getPupilId()).get(0) : null;
                    Mark mark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), finalDate, pupil.getPupilId()) ?
                            markRepository.findByCourseIdAndDateAndPupilId(course.getId(), finalDate, pupil.getPupilId()).get(0) : null;
                    Mark postMark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), postDate, pupil.getPupilId()) ?
                            markRepository.findByCourseIdAndDateAndPupilId(course.getId(), postDate, pupil.getPupilId()).get(0) : null;
                    Names names = namesRepository.findById(pupil.getPupilId()).get(0);
                    Account pupilAccount = loginRepository.findById(pupil.getPupilId()).get(0);

                    JSONObject pupilObject = new JSONObject();
                    pupilObject.put("name", names.getName() + " " + names.getSurname());
                    pupilObject.put("login", pupilAccount.getLogin());

                    JSONArray preMarkArray = new JSONArray();
                    preMarkArray.put(preMark == null ? null : preMark.getMarkA());
                    preMarkArray.put(preMark == null ? null : preMark.getMarkB());
                    preMarkArray.put(preMark == null ? null : preMark.getMarkC());
                    preMarkArray.put(preMark == null ? null : preMark.getTotal());
                    pupilObject.put("preMark", preMarkArray);

                    JSONArray markArray = new JSONArray();
                    markArray.put(mark == null ? null : mark.getMarkA());
                    markArray.put(mark == null ? null : mark.getMarkB());
                    markArray.put(mark == null ? null : mark.getMarkC());
                    markArray.put(mark == null ? null : mark.getTotal());
                    pupilObject.put("mark", markArray);

                    JSONArray postMarkArray = new JSONArray();
                    postMarkArray.put(postMark == null ? null : postMark.getMarkA());
                    postMarkArray.put(postMark == null ? null : postMark.getMarkB());
                    postMarkArray.put(postMark == null ? null : postMark.getMarkC());
                    postMarkArray.put(postMark == null ? null : postMark.getTotal());
                    pupilObject.put("postMark", postMarkArray);
                    pupilsArray.put(pupilObject);
                });
                courseObject.put("pupils", pupilsArray);
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
                Mark preMark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), preDate, pupil.getPupilId()) ?
                        markRepository.findByCourseIdAndDateAndPupilId(course.getId(), preDate, pupil.getPupilId()).get(0) : null;
                Mark mark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), date, pupil.getPupilId()) ?
                        markRepository.findByCourseIdAndDateAndPupilId(course.getId(), date, pupil.getPupilId()).get(0) : null;
                Mark postMark = markRepository.existsByCourseIdAndDateAndPupilId(course.getId(), postDate, pupil.getPupilId()) ?
                        markRepository.findByCourseIdAndDateAndPupilId(course.getId(), postDate, pupil.getPupilId()).get(0) : null;

                JSONArray preMarkArray = new JSONArray();
                preMarkArray.put(preMark == null ? null : preMark.getMarkA());
                preMarkArray.put(preMark == null ? null : preMark.getMarkB());
                preMarkArray.put(preMark == null ? null : preMark.getMarkC());
                preMarkArray.put(preMark == null ? null : preMark.getTotal());
                courseObject.put("preMark", preMarkArray);

                JSONArray markArray = new JSONArray();
                markArray.put(mark == null ? null : mark.getMarkA());
                markArray.put(mark == null ? null : mark.getMarkB());
                markArray.put(mark == null ? null : mark.getMarkC());
                markArray.put(mark == null ? null : mark.getTotal());
                courseObject.put("mark", markArray);

                JSONArray postMarkArray = new JSONArray();
                postMarkArray.put(postMark == null ? null : postMark.getMarkA());
                postMarkArray.put(postMark == null ? null : postMark.getMarkB());
                postMarkArray.put(postMark == null ? null : postMark.getMarkC());
                postMarkArray.put(postMark == null ? null : postMark.getTotal());
                courseObject.put("postMark", postMarkArray);

                array.put(courseObject);
            });
            //needTime + or - 604800000L
            model.addAttribute("data", array.toString());
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("setStudentRate")
    public String setStudentRate(Model model,
                                 @RequestParam String login,
                                 @RequestParam String password,
                                 @RequestParam String courseName,
                                 @RequestParam String date,
                                 @RequestParam String a,
                                 @RequestParam String b,
                                 @RequestParam String c,
                                 @RequestParam String pupilLogin) throws ParseException {
        int idOfCourse = courseRepository.findByName(courseName).get(0).getId();
        long courseDate = convertDate(date);

        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        Roles roles = rolesRepository.findById(account.getId()).get(0);
        if (roles.isTeacher() || roles.isAdmin()) {
            Mark mark =  !markRepository.existsByCourseIdAndDateAndPupilId(idOfCourse, courseDate, loginRepository.findByLogin(pupilLogin).get(0).getId()) ?
                    new Mark().setCourseId(idOfCourse).setPupilId(loginRepository.findByLogin(pupilLogin).get(0).getId()).setMarkA(Integer.parseInt(a))
                    .setMarkB(Integer.parseInt(b)).setMarkC(Integer.parseInt(c)).setDate(courseDate).setAdd(false).setTotal((int)((Integer.parseInt(a)+Integer.parseInt(b)+Integer.parseInt(c))/3)):
                    markRepository.findByCourseIdAndDateAndPupilId(idOfCourse, courseDate, loginRepository.findByLogin(pupilLogin).get(0).getId()).get(0);
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

    @PostMapping("/setHomework")
    public String setHomework(Model model,
                              @RequestParam String login,
                              @RequestParam String password,
                              @RequestParam int courseId,
                              @RequestParam String date,
                              @RequestParam String homework) throws ParseException {
        long homeworkDate = convertDate(date);
        boolean hasAccount = loginRepository.existsByLoginAndPassword(login, password);
        if (hasAccount){
            Roles roles = rolesRepository.findById(loginRepository.findByLoginAndPassword(login, password).get(0).getId()).get(0);
            if (roles.isAdmin() || roles.isTeacher()){
                Homework needHomework = homeworkRepository.existsByCourseIdAndDate(courseId, homeworkDate) ?
                        new Homework().setDate(homeworkDate).setCourseId(courseId).setHomework(homework) :
                        homeworkRepository.findByCourseIdAndDate(courseId, homeworkDate).get(0).setHomework(homework);
                homeworkRepository.save(needHomework);
                model.addAttribute("data", "true");
            }
            else {
                model.addAttribute("data", "Go daleko!");
            }
        }
        else {
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
                Names name = namesRepository.findById(pupil.getId()).get(0);
                pupilObject.put("name", name.getName());
                pupilObject.put("surname", name.getSurname());
                pupilObject.put("login", pupil.getLogin());
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
            accounts.forEach((teacher) -> {
                Roles role = rolesRepository.findById(teacher.getId()).get(0);
                if (!role.isTeacher()) return;
                JSONObject teacherObject = new JSONObject();
                Names name = namesRepository.findById(teacher.getId()).get(0);
                teacherObject.put("name", name.getName());
                teacherObject.put("surname", name.getSurname());
                teacherObject.put("login", teacher.getLogin());
                array.put(teacherObject);
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

    @PostMapping("/getAllAdminConfirmedBaskets")
    public String getAllAdminConfirmedBaskets(Model model,
                                         @RequestParam String login,
                                         @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();
            ArrayList<Basket> baskets = (ArrayList<Basket>) basketRepository.findAll();
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

    @PostMapping("/setStatus")
    public String setStatus(Model model,
                            @RequestParam String login,
                            @RequestParam String password,
                            @RequestParam int basketId,
                            @RequestParam String status){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            Status basketStatus = statusRepository.findById(basketId).get(0).setStatus(status);
            statusRepository.save(basketStatus);
            model.addAttribute("data", "true");
        }
        else {
            model.addAttribute("data", "Go daleko!");
        }
        return "androidData";
    }

    @PostMapping("/getAllUnconfirmedBaskets")
    public String getAllUnconfirmedBaskets(Model model,
                                         @RequestParam String login,
                                         @RequestParam String password){
        Account account = loginRepository.findByLoginAndPassword(login, password).isEmpty() ?
                null : loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null){
            JSONArray array = new JSONArray();

            if (unconfirmedBasketRepository.existsByCustomerId(account.getId())) {
                ArrayList<UnconfirmedBasket> baskets = (ArrayList<UnconfirmedBasket>) unconfirmedBasketRepository.findByCustomerId(account.getId());
                baskets.forEach((basket) -> {
                    JSONObject basketObject = new JSONObject();
                    basketObject.put("id", basket.getId());
                    basketObject.put("product", basket.getProductId());
                    basketObject.put("imageUrl", productRepository.findById(basket.getProductId()).get(0).getImgSrc());
                    basketObject.put("cost", productRepository.findById(basket.getProductId()).get(0).getCost());
                    basketObject.put("productName", productRepository.findById(basket.getProductId()).get(0).getTitle());
                    array.put(basketObject);
                });
                model.addAttribute("data", array.toString());
            }
            else {
                model.addAttribute("data", "Nothing!");
            }
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
            if (productRepository.findById(basket.getProductId()).get(0).getCount() > 0){
                Basket confirmedBasket = new Basket().setCustomerId(basket.getCustomerId()).setProductId(basket.getProductId());
                basketRepository.save(confirmedBasket);

                Status status = new Status().setId(confirmedBasket.getId()).setStatus("Не прочитано");
                statusRepository.save(status);

                unconfirmedBasketRepository.delete(basket);
                    productRepository.save(productRepository.findById(basket.getProductId()).get(0).setCount(productRepository.findById(basket.getProductId()).get(0).getCount()-1));
                model.addAttribute("data", "Good");

            }
            else {
                model.addAttribute("data", "Not enough");
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
                               @RequestParam String date,
                               @RequestParam String[] pupils,
                               @RequestParam String teacher,
                               @RequestParam String imgSrc) throws ParseException {
        if (!loginRepository.findByLoginAndPassword(login, password).isEmpty()){
            Course course = new Course();

            Account teacherAccount = loginRepository.findByLogin(teacher).get(0);
            Names teacherName = namesRepository.findById(teacherAccount.getId()).get(0);
            System.out.println(convertDate(date));
            System.out.println(date);
            course.setTeacherId(teacherAccount.getId())
                    .setDate(convertDate(date))
                    .setImgSrc(imgSrc)
                    .setName(name)
                    .setTeacherName(teacherName.getName() + " " + teacherName.getSurname())
                    .setNextDate(date);
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
