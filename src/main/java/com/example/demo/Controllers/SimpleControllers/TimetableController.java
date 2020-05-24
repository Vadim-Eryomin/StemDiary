package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.Homework;
import com.example.demo.Domain.Mark;
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

@Controller
public class TimetableController {
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    NamesRepository namesRepository;
    @Autowired
    MarkRepository markRepository;
    @Autowired
    HomeworkRepository homeworkRepository;
    @Autowired
    PupilRepository pupilRepository;
    @Autowired
    LoginRepository loginRepository;

    String humanId;
    Model model;
    int id;
    long date;
    int markA;
    int markB;
    int markC;
    int total;


    @GetMapping("/timetable")
    public String showTimetable(@CookieValue(defaultValue = "noname") String humanId, Model model,
                                @RequestParam(required = false, defaultValue = "0") int id,
                                @RequestParam(required = false, defaultValue = "0") long date) throws ParseException {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        if (id == 0) {
            //prepare model for page
            ModelPreparer.prepareShowingTimetable(this);
            return "timetable";
        } else {
            //set course id
            this.id = id;
            this.date = date;
            //prepare lesson page
            ModelPreparer.prepareLesson(this);
            return "lesson";
        }
    }

    @GetMapping("/homeworkEdit")
    public String showHomework(@CookieValue(defaultValue = "noname") String humanId, Model model,
                               @RequestParam int id, @RequestParam long date) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        this.id = id;
        this.date = date;

        //prepare model for page
        ModelPreparer.prepareHomeworkEditing(this);

        return "homeworkEdit";
    }

    @PostMapping("/homeworkEdit")
    public String editHomework(@CookieValue(defaultValue = "noname") String humanId, Model model,
                               @RequestParam int id, @RequestParam long date, @RequestParam String homeworkDef) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        //id is course id
        this.id = id;
        this.date = date;

        System.out.println(id);
        System.out.println(date);

        Homework homework = homeworkRepository.findByCourseId(id).isEmpty() ? new Homework() : homeworkRepository.findByCourseId(id).get(0);
        homework.setHomework(homeworkDef).setDate(date).setCourseId(id);
        homeworkRepository.save(homework);

        //prepare model for page
        ModelPreparer.prepareShowingTimetable(this);

        return "redirect:/timetable";
    }

    @GetMapping("/markEdit")
    public String showMarks(@CookieValue(defaultValue = "noname") String humanId, Model model,
                            @RequestParam int courseId, @RequestParam long date) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        //id is course id
        this.id = courseId;
        this.date = date;

        ModelPreparer.prepareMarkEditing(this);

        return "markEdit";
    }

    @PostMapping("/markEdit")
    public String editMarks(@CookieValue(defaultValue = "noname") String humanId, Model model,
                            @RequestParam int courseId, @RequestParam long date,
                            @RequestParam int markA, @RequestParam int markB,
                            @RequestParam int markC, @RequestParam int pupilId) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        //id is course id
        this.id = courseId;
        this.date = date;

        Mark mark = markRepository.findByCourseIdAndDate(courseId, date).isEmpty() ?
                new Mark().setCourseId(courseId).setDate(date).setPupilId(pupilId) :
                markRepository.findByCourseIdAndDate(courseId, date).get(0);

        mark.setMarkA(markA).setMarkB(markB).setMarkC(markC).setTotal((int) ((markA + markB + markC) / 3));
        markRepository.save(mark);

        ModelPreparer.prepareMarkEditing(this);

        return "markEdit";
    }

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }

    public TimetableController setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
        return this;
    }

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public TimetableController setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        return this;
    }

    public ColorRepository getColorRepository() {
        return colorRepository;
    }

    public TimetableController setColorRepository(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
        return this;
    }

    public NamesRepository getNamesRepository() {
        return namesRepository;
    }

    public TimetableController setNamesRepository(NamesRepository namesRepository) {
        this.namesRepository = namesRepository;
        return this;
    }

    public MarkRepository getMarkRepository() {
        return markRepository;
    }

    public TimetableController setMarkRepository(MarkRepository markRepository) {
        this.markRepository = markRepository;
        return this;
    }

    public HomeworkRepository getHomeworkRepository() {
        return homeworkRepository;
    }

    public TimetableController setHomeworkRepository(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
        return this;
    }

    public String getHumanId() {
        return humanId;
    }

    public TimetableController setHumanId(String humanId) {
        this.humanId = humanId;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public TimetableController setModel(Model model) {
        this.model = model;
        return this;
    }

    public int getId() {
        return id;
    }

    public TimetableController setId(int id) {
        this.id = id;
        return this;
    }

    public long getDate() {
        return date;
    }

    public TimetableController setDate(long date) {
        this.date = date;
        return this;
    }

    public PupilRepository getPupilRepository() {
        return pupilRepository;
    }

    public TimetableController setPupilRepository(PupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
        return this;
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public TimetableController setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        return this;
    }
}
