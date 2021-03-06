package com.example.demo.Controllers.SimpleControllers;

import com.example.demo.Domain.Homework;
import com.example.demo.Domain.Mark;
import com.example.demo.Domain.StemCoin;
import com.example.demo.HelpClasses.ModelPreparer;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    StemCoinRepository stemCoinRepository;

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
            System.out.println("we're in lesson!");
            //set course id
            System.out.println("set data");
            this.id = id;
            this.date = date;
            System.out.println("data was set");
            //prepare lesson page
            System.out.println("prepare model");
            ModelPreparer.prepareLesson(this);
            System.out.println("model was prepared");
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
                               @RequestParam int id, @RequestParam long date, @RequestParam String homeworkDef,
                               RedirectAttributes attributes) {
        //if we don't see login cookie redirect to login page
        if (humanId.equals("noname")) return "redirect:/login";

        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        //id is course id
        this.id = id;
        this.date = date;

        Homework homework = homeworkRepository.findByCourseIdAndDate(id, date).isEmpty() ? new Homework() : homeworkRepository.findByCourseIdAndDate(id, date).get(0);
        homework.setHomework(homeworkDef).setDate(date).setCourseId(id);
        homeworkRepository.save(homework);

        //prepare model for page
        ModelPreparer.prepareShowingTimetable(this);
        attributes.addAttribute("date", date);
        attributes.addAttribute("id", id);

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
        System.out.println(markA + " " + markB + " " + markC);
        //set data for data preparer
        this.model = model;
        this.humanId = humanId;
        //id is course id
        this.id = courseId;
        this.date = date;
        System.out.println("set mark");
        Mark mark = markRepository.findByCourseIdAndDateAndPupilId(courseId, date, pupilId).isEmpty() ?
                new Mark().setCourseId(courseId).setDate(date).setPupilId(pupilId).setAdd(false) :
                markRepository.findByCourseIdAndDateAndPupilId(courseId, date, pupilId).get(0);
        System.out.println(mark.toString());
        mark.setMarkA(markA).setMarkB(markB).setMarkC(markC).setTotal((int) ((markA + markB + markC) / 3)).setAdd(true);
        StemCoin stemCoin = stemCoinRepository.existsById(pupilId) ? stemCoinRepository.findById(pupilId).get(0).setStemcoins(stemCoinRepository.findById(pupilId).get(0).getStemcoins() + mark.getTotal()) : new StemCoin().setId(pupilId).setStemcoins(mark.getTotal());
        stemCoinRepository.save(stemCoin);
        System.out.println(mark.toString());
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
