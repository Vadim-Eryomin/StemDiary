package com.example.demo.Controllers;

import com.example.demo.Domain.*;
import com.example.demo.Domain.JSONDomain.JSONCourse;
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

    @GetMapping("/getPupilCourses")
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

                courseData.setPupilId(account.getId());
                courseData.setTeacherId(teacher.getId());
                courseData.setTeacherName(teacher.getName() + " " + teacher.getSurname());
                courseData.setAvatarUrl(course.getImgSrc());

                long date = course.getDate();
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

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                courseData.setPreDate(format.format(preDate));
                courseData.setDate(format.format(date));
                courseData.setPostDate(format.format(postDate));

                JSONObject courseObject = new JSONObject();
                courseObject.put("pupilId", courseData.getPupilId());
                courseObject.put("teacherId", courseData.getTeacherId());
                courseObject.put("teacherName", courseData.getTeacherName());
                courseObject.put("avatarUrl", courseData.getAvatarUrl());
                courseObject.put("preHomework", courseData.getPreHomework());
                courseObject.put("homework", courseData.getHomework());
                courseObject.put("postHomework", courseData.getPostHomework());
                courseObject.put("preDate", courseData.getPreDate());
                courseObject.put("date", courseData.getDate());
                courseObject.put("postDate", courseData.getPostDate());

                array.put(courseObject);
            });
            //needTime + or - 604800000L
            model.addAttribute("data", array.toString());
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

    public static long convertDate(String date) throws ParseException {
        //dd.MM.YYYY HH:mm
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.parse(date).getTime();
    }


}
