package com.example.demo.HelpClasses;

import com.example.demo.Controllers.AdminControllers.*;
import com.example.demo.Controllers.SimpleControllers.*;
import com.example.demo.Domain.*;
import com.example.demo.Domain.ModelDomain.BasketModelProduct;
import com.example.demo.Domain.ModelDomain.PupilWithNameAndLastMark;
import com.example.demo.Repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;

@Controller
public class ModelPreparer {

    public static void prepare(ProfileController c) {
        LoginRepository loginRepository = c.getLoginRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        RolesRepository rolesRepository = c.getRolesRepository();

        Model model = c.getModel();
        int humanId = Integer.parseInt(c.getHumanId());

        Account account = loginRepository.findById(humanId).get(0);
        ColorScheme color = colorRepository.findById(humanId).get(0);
        Names names = namesRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("login", account.getLogin());
        model.addAttribute("password", account.getPassword());
        model.addAttribute("navColor", ColorTranslator.translateColor(color.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(color.getBodyColor()));
        model.addAttribute("name", names.getName());
        model.addAttribute("surname", names.getSurname());
        model.addAttribute("imgSrc", account.getImgSrc());

        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(HomeController c) {
        RolesRepository rolesRepository = c.getRolesRepository();

        Model model = c.getModel();
        int humanId = Integer.parseInt(c.getHumanId());

        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(NewsController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();

        Model model = c.getModel();
        int humanId = Integer.parseInt(c.getHumanId());

        Roles roles = rolesRepository.findById(humanId).get(0);
        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());

    }

    public static void prepare(ShopController c) {
        ProductRepository productRepository = c.getProductRepository();
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        String title = c.getTitle();
        int cost = c.getCost();

        Roles roles = rolesRepository.findById(humanId).get(0);
        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAll();

        if (!title.equals("") && cost != 0) {
            products = (ArrayList<Product>) productRepository.findByCostAndTitleContainingIgnoreCase(cost, title);
        } else if (!title.equals("")) {
            products = (ArrayList<Product>) productRepository.findByTitleContainingIgnoreCase(title);
        } else if (cost != 0) {
            products = (ArrayList<Product>) productRepository.findByCost(cost);
        }

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("products", products);
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(BasketController c) {
        ProductRepository productRepository = c.getProductRepository();
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        StatusRepository statusRepository = c.getStatusRepository();
        BasketRepository basketRepository = c.getBasketRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        Roles roles = rolesRepository.findById(humanId).get(0);
        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        ArrayList<Basket> basketProducts = (ArrayList<Basket>) basketRepository.findByCustomerId(humanId);
        ArrayList<BasketModelProduct> basketModelProducts = new ArrayList<>();

        for (int i = 0; i < basketProducts.size(); i++) {
            BasketModelProduct product = new BasketModelProduct();
            int status = statusRepository.findById(basketProducts.get(i).getId()).get(0).getStatus();
            product.setProductName(productRepository.findById(basketProducts.get(i).getProductId()).get(0).getTitle()).setStatus(status == 0 ? "Заказ не обработан" : status == 1 ? "Заказ в работе" : status == 2 ? "Заказ отменен" : status == 3 ? "Заказ готов" : status == 4 ? "Заказ выдан" : "хз");
            basketModelProducts.add(product);
        }

        model.addAttribute("products", basketModelProducts);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(MainTimetableAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        CourseRepository courseRepository = c.getCourseRepository();
        NamesRepository namesRepository = c.getNamesRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<Course> courses = (ArrayList<Course>) courseRepository.findAll();

        courses.forEach((course) -> {
            Names teacherNames = namesRepository.findById(course.getTeacherId()).get(0);
            course.setTeacherName(teacherNames.getName() + " " + teacherNames.getSurname());

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm");
            Date now = new Date();
            if (course.getNextDate() == null) {
                course.setNextDate(format.format(course.getDate()));
            }
            //set the nearest date of next lesson
            while (true) {
                try {
                    if (!(now.getTime() < format.parse(course.getNextDate()).getTime())) break;
                    else {
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(format.parse(course.getNextDate()));
                        calendar.add(Calendar.DAY_OF_MONTH, 7);
                        course.setNextDate(format.format(calendar.getTime()));
                        System.out.println(course.getNextDate());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        model.addAttribute("courses", courses);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(MainShopAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        ProductRepository productRepository = c.getProductRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(MainAccountsAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<Names> names = (ArrayList<Names>) namesRepository.findAll();

        model.addAttribute("names", names);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareAccountCreation(MainAccountsAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareAccountEdition(MainAccountsAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        LoginRepository loginRepository = c.getLoginRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int id = c.getId();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(id).get(0);
        Roles myRoles = rolesRepository.findById(humanId).get(0);
        Names names = namesRepository.findById(id).get(0);
        Account account = loginRepository.findById(id).get(0);

        model.addAttribute("id", names.getId());
        model.addAttribute("imgSrc", account.getImgSrc() == null ? "image/just.png" : account.getImgSrc());
        model.addAttribute("name", names.getName());
        model.addAttribute("surname", names.getSurname());
        model.addAttribute("login", account.getLogin());
        model.addAttribute("password", account.getPassword());
        model.addAttribute("admin", roles.isAdmin());
        model.addAttribute("teacher", roles.isTeacher());

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", myRoles.isAdmin());
    }

    public static void prepare(MainAdminPanelController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareTimetableCreation(MainTimetableAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        PupilRepository pupilRepository = c.getPupilRepository();
        NamesRepository namesRepository = c.getNamesRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<Roles> accountsByRoles = (ArrayList<Roles>) rolesRepository.findAll();
        ArrayList<Names> pupils = new ArrayList<>();
        ArrayList<Names> teachers = new ArrayList<>();

        accountsByRoles.forEach(new Consumer<Roles>() {
            @Override
            public void accept(Roles roles) {
                if (roles.isAdmin())
                    return;
                if (roles.isTeacher())
                    teachers.add(namesRepository.findById(roles.getId()).get(0));
                else {
                    pupils.add(namesRepository.findById(roles.getId()).get(0));
                }
            }
        });

        model.addAttribute("teachers", teachers);
        model.addAttribute("pupils", pupils);

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareTimetableEdition(MainTimetableAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        CourseRepository courseRepository = c.getCourseRepository();
        PupilRepository pupilRepository = c.getPupilRepository();
        NamesRepository namesRepository = c.getNamesRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int courseId = c.getId();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        Course course = courseRepository.findById(courseId).get(0);
        ArrayList<Roles> accountsByRoles = (ArrayList<Roles>) rolesRepository.findAll();
        ArrayList<Names> pupils = new ArrayList<>();
        ArrayList<Names> teachers = new ArrayList<>();

        accountsByRoles.forEach(new Consumer<Roles>() {
            @Override
            public void accept(Roles roles) {
                if (roles.isAdmin())
                    return;
                if (roles.isTeacher())
                    teachers.add(namesRepository.findById(roles.getId()).get(0));
                else {
                    pupils.add(namesRepository.findById(roles.getId()).get(0));
                }

            }
        });

        ArrayList<Pupil> oldPupils = (ArrayList<Pupil>) pupilRepository.findByCourseId(courseId);
        ArrayList<Names> oldPupilsNames = new ArrayList<>();

        for (Pupil old :
                oldPupils) {
            if (pupils.isEmpty())
                break;
            for (int i = pupils.size() - 1; i >= 0; i--) {
                if (old.getPupilId() == pupils.get(i).getId()) {
                    pupils.remove(i);
                }
            }
        }

        for (Pupil p :
                oldPupils) {
            oldPupilsNames.add(namesRepository.findById(p.getPupilId()).get(0));
        }

        // just time in text
        String time = new SimpleDateFormat("YYYY-MM-dd HH:mm").format(course.getDate());
        //convert to Chrome type
        time = time.split(" ")[0] + "T" + time.split(" ")[1];

        model.addAttribute("id", course.getId());
        model.addAttribute("name", course.getName());
        model.addAttribute("course", course);
        model.addAttribute("teachers", teachers);
        model.addAttribute("pupils", pupils);
        model.addAttribute("oldPupils", oldPupilsNames);
        model.addAttribute("time", time);
        model.addAttribute("url", course.getImgSrc());
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareShopCreation(MainShopAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareShopEdition(MainShopAdminController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        ProductRepository productRepository = c.getProductRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int productId = c.getId();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        Product product = productRepository.findById(productId).get(0);

        model.addAttribute("title", product.getTitle());
        model.addAttribute("about", product.getAbout());
        model.addAttribute("imgSrc", product.getImgSrc());
        model.addAttribute("cost", product.getCost());
        model.addAttribute("id", product.getId());

        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareShowingTimetable(TimetableController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        CourseRepository courseRepository = c.getCourseRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        PupilRepository pupilRepository = c.getPupilRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<Course> courses;

        if (roles.isAdmin()) {
            courses = (ArrayList<Course>) courseRepository.findAll();
        } else if (roles.isTeacher()) {
            courses = (ArrayList<Course>) courseRepository.findByTeacherId(humanId);
        } else {
            ArrayList<Pupil> IAmAPupil = (ArrayList<Pupil>) pupilRepository.findByPupilId(humanId);
            courses = new ArrayList<>();
            IAmAPupil.forEach((pupil)->{
                courses.add(courseRepository.findById(pupil.getCourseId()).get(0));
            });
        }

        if (courses != null)
            courses.forEach((course) -> {
                Names teacherNames = namesRepository.findById(course.getTeacherId()).get(0);
                course.setTeacherName(teacherNames.getName() + " " + teacherNames.getSurname());

                if(course.getImgSrc() == null || course.getImgSrc().equals("")){
                    course.setImgSrc("image/just.png");
                }

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm");
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
                } catch (Exception ignored) {}

                courseRepository.save(course);

            });

        model.addAttribute("course", courses);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepareLesson(TimetableController c) throws ParseException {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        CourseRepository courseRepository = c.getCourseRepository();
        HomeworkRepository homeworkRepository = c.getHomeworkRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        LoginRepository loginRepository = c.getLoginRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int courseId = c.getId();
        long courseDate = c.getDate();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        Course course = courseRepository.findById(courseId).get(0);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        System.out.println(course);
        Date date = df.parse(course.getNextDate());

        ArrayList<Homework> homeworks = (ArrayList<Homework>) homeworkRepository.findByCourseIdAndDate(courseId, courseDate == 0 ? date.getTime() : courseDate);
        Homework homework = (homeworks.isEmpty() ? new Homework().setHomework("Похоже, пока ничего нет!") : homeworks.get(0));

        Names teacher = namesRepository.findById(course.getTeacherId()).get(0);
        String teacherName = teacher.getName() + " " + teacher.getSurname();


        model.addAttribute("homework", homework.getHomework());
        model.addAttribute("pre", courseDate == 0 ? date.getTime() : courseDate - 604800000);
        model.addAttribute("post", courseDate == 0 ? date.getTime() : courseDate + 604800000);
//        model.addAttribute("id", homework.getId());
        model.addAttribute("date", date.getTime());
        model.addAttribute("courseId", courseId);
        model.addAttribute("teacherName", teacherName);
        model.addAttribute("teacherImgSrc", loginRepository.findById(teacher.getId()).get(0).getImgSrc());
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
        model.addAttribute("teacher", roles.isTeacher());
    }

    public static void prepareHomeworkEditing(TimetableController c) {
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        HomeworkRepository homeworkRepository = c.getHomeworkRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int courseId = c.getId();
        long date = c.getDate();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

//        SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        Date homeworkDate = new Date(date);
        ArrayList<Homework> homeworks = (ArrayList<Homework>) homeworkRepository.findByCourseIdAndDate(courseId, homeworkDate.getTime());
        System.out.println(courseId);
        System.out.println(date);
        System.out.println(homeworks.toString());
        Homework homework = (homeworks.isEmpty() ? new Homework().setHomework("") : homeworks.get(0));
        System.out.println(homework.toString());

        model.addAttribute("homework", homework.getHomework());
        model.addAttribute("id", homework.getId());
        model.addAttribute("date", date);
        model.addAttribute("courseId", courseId);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
        model.addAttribute("teacher", roles.isTeacher());
    }

    public static void prepareMarkEditing(TimetableController c){
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        PupilRepository pupilRepository = c.getPupilRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        MarkRepository  markRepository =  c.getMarkRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int courseId = c.getId();
        long date = c.getDate();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<PupilWithNameAndLastMark> pupils = new ArrayList<>();
        ArrayList<Pupil> pupilIds = roles.isAdmin() || roles.isTeacher() ?
                (ArrayList<Pupil>) pupilRepository.findByCourseId(courseId) :
                null;
        if (!roles.isAdmin() && !roles.isTeacher()){
            pupilIds = new ArrayList<>();
            pupilIds.add(pupilRepository.findByCourseIdAndPupilId(courseId, humanId).get(0));
        }
        if (!pupilIds.isEmpty())
            pupilIds.forEach((pupil) -> {
                PupilWithNameAndLastMark modelPupil = new PupilWithNameAndLastMark();
                Names names = namesRepository.findById(pupil.getPupilId()).get(0);
                Mark mark = markRepository.findByCourseIdAndDateAndPupilId(courseId, date, pupil.getPupilId()).isEmpty() ?
                        null :
                        markRepository.findByCourseIdAndDateAndPupilId(courseId, date, pupil.getPupilId()).get(0);
                modelPupil.setId(pupil.getId())
                        .setCourseId(pupil.getCourseId())
                        .setPupilId(pupil.getPupilId())
                        .setName(names.getName())
                        .setSurname(names.getSurname())
                        .setMarkA(mark == null ? 0 : mark.getMarkA())
                        .setMarkB(mark == null ? 0 : mark.getMarkB())
                        .setMarkC(mark == null ? 0 : mark.getMarkC());
                pupils.add(modelPupil);
            });

        model.addAttribute("pupil", pupils);
        model.addAttribute("courseId", courseId);
        model.addAttribute("date", date);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
        model.addAttribute("teacher", roles.isTeacher());
    }
    public static void prepare(MainRegisterRequestAdminController c){
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        RegisterRequestRepository registerRequestRepository = c.getRegisterRequestRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        ArrayList<RegisterRequest> registerRequests = (ArrayList<RegisterRequest>) registerRequestRepository.findAll();

        model.addAttribute("registerRequests", registerRequests);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }
    public static void prepareRequestEditing(MainRegisterRequestAdminController c){
        RolesRepository rolesRepository = c.getRolesRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        RegisterRequestRepository registerRequestRepository = c.getRegisterRequestRepository();

        int humanId = Integer.parseInt(c.getHumanId());
        Model model = c.getModel();
        int id = c.getId();

        ColorScheme colorScheme = colorRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);
        RegisterRequest request = registerRequestRepository.findById(id).get(0);

        model.addAttribute("request", request);
        model.addAttribute("navColor", ColorTranslator.translateColor(colorScheme.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(colorScheme.getBodyColor()));
        model.addAttribute("admin", roles.isAdmin());
    }

    public static void prepare(ErrorController c){
        LoginRepository loginRepository = c.getLoginRepository();
        ColorRepository colorRepository = c.getColorRepository();
        NamesRepository namesRepository = c.getNamesRepository();
        RolesRepository rolesRepository = c.getRolesRepository();

        Model model = c.getModel();
        int humanId = Integer.parseInt(c.getHumanId());

        Account account = loginRepository.findById(humanId).get(0);
        ColorScheme color = colorRepository.findById(humanId).get(0);
        Names names = namesRepository.findById(humanId).get(0);
        Roles roles = rolesRepository.findById(humanId).get(0);

        model.addAttribute("navColor", ColorTranslator.translateColor(color.getNavigationColor()));
        model.addAttribute("bodyColor", ColorTranslator.translateColor(color.getBodyColor()));

        System.out.println(ColorTranslator.translateColor(color.getBodyColor()));
        System.out.println(ColorTranslator.translateColor(color.getNavigationColor()));
        model.addAttribute("name", names.getName());
        model.addAttribute("surname", names.getSurname());
        model.addAttribute("imgSrc", account.getImgSrc());

        model.addAttribute("admin", roles.isAdmin());

    }


}
