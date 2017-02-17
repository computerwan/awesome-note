import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pengcheng.wan on 2017/2/17.
 */
public class Test {
    public static void main(String[] args) {
        Calendar start =Calendar.getInstance();
        start.set(2016,6,1);
        Long startTIme = start.getTimeInMillis();

        Calendar end=Calendar.getInstance();
        end.set(2017,1,31);
        Long endTime = end.getTimeInMillis();

        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(df.format(d));
            time += oneDay;
        }

    }


}
