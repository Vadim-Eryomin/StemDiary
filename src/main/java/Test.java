import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        String sDate1 = "17.05.2020 16:00";
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(sDate1);
        System.out.println(sDate1 + "\t" + (new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(date1.getTime()+604800000))));
    }
}
