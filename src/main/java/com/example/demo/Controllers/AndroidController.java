package com.example.demo.Controllers;

import com.example.demo.Domain.*;
import com.example.demo.Repositories.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
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


    @PostMapping("/androidLogin")
    public String login(Model model,
                        @RequestParam String login,
                        @RequestParam String password) {
        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            Names names = namesRepository.findById(account.getId()).get(0);
            Roles roles = rolesRepository.findById(account.getId()).get(0);

            JsonObject name = new JsonObject();
            name.addProperty("name", names.getName());
            name.addProperty("surname", names.getSurname());
            name.addProperty("admin", roles.isAdmin());
            name.addProperty("teacher", roles.isTeacher());

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
            ArrayList<Course> courses = (ArrayList<Course>) courseRepository.findByTeacherId(account.getId());
            JsonArray array = new JsonArray();
            courses.forEach((course) -> {
                JsonObject object = new JsonObject();
                object.addProperty("courseName", course.getName());
                array.add(object);
            });

            model.addAttribute("data", array);
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @PostMapping("/getCourseLessons")
    public String getCourseLessons(Model model,
                                   @RequestParam String login,
                                   @RequestParam String password,
                                   @RequestParam String courseName) {
        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            JsonObject dates = new JsonObject();

            Course course = courseRepository.findByName(courseName).get(0);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            Date now = new Date();
            if (course.getNextDate() == null) {
                course.setNextDate(format.format(course.getDate()));
            }
            //set the nearest date of next lesson
            try {
                while (now.after(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()))) {
                    // TODO: 20.05.2020 оптимизировать!!! добавляет неделю, но выглядит жутковато)
                    course.setNextDate(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()).getTime() + 604800000)));
                }
            } catch (Exception ignored) {
            }

            try {
                long needTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(course.getNextDate()).getTime() - 604800000L;
                Date date = new Date(needTime);
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
                dates.addProperty("pre", formattedDate);

                date = new Date(needTime + 604800000L);
                formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
                dates.addProperty("lesson", formattedDate);

                date = new Date(needTime + 604800000L);
                formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
                dates.addProperty("post", formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            model.addAttribute("data", dates.toString());
        } else {
            model.addAttribute("data", "Go daleko!");
        }


        return "androidData";
    }

    @PostMapping("/getLessonStudents")
    public String getLessonStudents(Model model,
                                    @RequestParam String login,
                                    @RequestParam String password,
                                    @RequestParam String courseName,
                                    @RequestParam String date) throws ParseException {
        int idOfCourse = courseRepository.findByName(courseName).get(0).getId();
        long courseDate = convertDate(date);

        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            Course course = courseRepository.findById(idOfCourse).get(0);
            ArrayList<Pupil> pupils = (ArrayList<Pupil>) pupilRepository.findByCourseId(idOfCourse);

            JsonArray array = new JsonArray();
            pupils.forEach((pupil) -> {
                Names pupilNames = namesRepository.findById(pupil.getPupilId()).get(0);
                Account pupilAccount = loginRepository.findById(pupil.getPupilId()).get(0);

                JsonObject pupilObject = new JsonObject();
                pupilObject.addProperty("name", pupilNames.getName());
                pupilObject.addProperty("surname", pupilNames.getSurname());
                pupilObject.addProperty("login", pupilAccount.getLogin());

                array.add(pupilObject);
            });

            model.addAttribute("data", array);
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    @GetMapping("setStudentRate/{login}/{password}/{courseName}/{date}/{a}/{b}/{c}")
    public String setStudentRate(Model model,
                                 @PathVariable String login,
                                 @PathVariable String password,
                                 @PathVariable String courseName,
                                 @PathVariable String date,
                                 @PathVariable String a,
                                 @PathVariable String b,
                                 @PathVariable String c) throws ParseException {
        int idOfCourse = courseRepository.findByName(courseName).get(0).getId();
        long courseDate = convertDate(date);

        Account account = loginRepository.findByLoginAndPassword(login, password).get(0);
        if (account != null) {
            Course course = courseRepository.findById(idOfCourse).get(0);
            ArrayList<Pupil> pupils = (ArrayList<Pupil>) pupilRepository.findByCourseId(idOfCourse);

            JsonArray array = new JsonArray();
            pupils.forEach((pupil) -> {
                Names pupilNames = namesRepository.findById(pupil.getPupilId()).get(0);
                array.add(pupilNames.getName() + " " + pupilNames.getSurname());
            });

            model.addAttribute("data", array);
        } else {
            model.addAttribute("data", "Go daleko!");
        }

        return "androidData";
    }

    public static long convertDate(String date) throws ParseException {
        //dd.MM.YYYY HH:mm
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.parse(date).getTime();
    }


}
