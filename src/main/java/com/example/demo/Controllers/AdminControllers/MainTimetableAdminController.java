package com.example.demo.Controllers.AdminControllers;

import com.example.demo.Domain.Course;
import com.example.demo.Domain.Pupil;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

@Controller
public class MainTimetableAdminController {
    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    NamesRepository namesRepository;

    @Autowired
    PupilRepository pupilRepository;

    Model model;
    String humanId;

    //id of course for course edition
    int id;

    @GetMapping("/adminTimetable")
    public String showTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        ModelPreparer.prepare(this);

        return "adminTimetable";
    }

    @GetMapping("/adminTimetableCreate")
    public String showTimetableCreating(@CookieValue(defaultValue = "noname") String humanId, Model model) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        //different preparer for creation page
        ModelPreparer.prepareTimetableCreation(this);

        return "timetableCreate";
    }

    @PostMapping("/adminTimetableCreate")
    public String createTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                  @RequestParam int teacherId, @RequestParam(required = false) int[] pupilId,
                                  @RequestParam String name, @RequestParam String url,
                                  @RequestParam String time) throws ParseException {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;

        Course course = new Course();
        course.setName(name).setTeacherId(teacherId)
                .setDate(new SimpleDateFormat("YYYY-MM-DD HH:mm").parse(time.split("T")[0] + " " + time.split("T")[1]).getTime())
                .setImgSrc(url);
        courseRepository.save(course);

        if (pupilId != null)
            for (Integer id :
                    pupilId) {
                Pupil pupil = new Pupil();
                pupil.setCourseId(course.getId()).setPupilId(id);
                pupilRepository.save(pupil);
            }

        ModelPreparer.prepare(this);

        return "redirect:/adminTimetable";
    }

    @GetMapping("/adminTimetableEdit")
    public String showTimetableEditing(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                       @RequestParam int id) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        this.id = id;

        //different preparer for edition page
        ModelPreparer.prepareTimetableEdition(this);

        return "timetableEdit";
    }

    @PostMapping("/adminTimetableEdit")
    public String editTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                @RequestParam int id, @RequestParam(defaultValue = "noname") String name,
                                @RequestParam(defaultValue = "noname") String teacherId,
                                @RequestParam(required = false) int[] pupilsId, @RequestParam String url,
                                @RequestParam String time) throws ParseException {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        //if we cant find any elements
        if (name.equals("noname") || teacherId.equals("noname"))
            return "redirect:/timetableEdit";

        //else, if we have all need
        //prepare model for next page
        ModelPreparer.prepare(this);

        //set all data for course and pupils
        Course course = courseRepository.findById(id).get(0);
        course.setTeacherId(Integer.parseInt(teacherId)).setName(name)
                .setDate(new SimpleDateFormat("YYYY-MM-DD HH:mm").parse(time.split("T")[0] + " " + time.split("T")[1]).getTime())
                .setImgSrc(url);
        courseRepository.save(course);

        //delete all pupils from this course and rewrite them
        ArrayList<Pupil> deletePupils = (ArrayList<Pupil>) pupilRepository.findByCourseId(course.getId());

        for (Pupil p :
                deletePupils) {
            pupilRepository.delete(p);
        }

        if (pupilsId != null) {
            for (Integer pupilId :
                    pupilsId) {
                Pupil pupil = new Pupil();
                pupil.setPupilId(rolesRepository.findById(pupilId.intValue()).get(0).getId())
                        .setCourseId(id);
                pupilRepository.save(pupil);
            }
        }


        return "redirect:/adminTimetable";
    }

    @GetMapping("/adminTimetableDelete")
    public String deleteTimetable(@RequestParam int id, @CookieValue(defaultValue = "noname") String humanId, Model model){
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";
        //set data for model preparer
        this.humanId = humanId;
        this.model = model;
        //prepare model for page
        ModelPreparer.prepare(this);

        Course course = courseRepository.findById(id).get(0);
        ArrayList<Pupil> pupils = (ArrayList<Pupil>) pupilRepository.findByCourseId(id);
        courseRepository.delete(course);
        for (Pupil p :
                pupils) {
            pupilRepository.delete(p);
        }

        return "redirect:/adminTimetable";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public MainTimetableAdminController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public MainTimetableAdminController setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public MainTimetableAdminController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public MainTimetableAdminController setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public MainTimetableAdminController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public MainTimetableAdminController setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
        return this;
    }

    public PupilRepository getPupilRepository() {
        return pupilRepository;
    }

    public MainTimetableAdminController setPupilRepository(PupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
        return this;
    }

    public int getId() {
        return id;
    }

    public MainTimetableAdminController setId(int id) {
        this.id = id;
        return this;
    }
}
