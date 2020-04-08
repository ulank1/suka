package kg.docplus.ui.main.filter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Filter {

    public static ArrayList<Integer> services = new ArrayList<>();
    public static int service = 0;
    public static int max_price = 10000;
    public static int min_price = 0;
    public static String schedule_time_before = "22:00";
    public static String schedule_time_after = "01:00";
    public static String name = "";
    public static String specialty_title = "";
    public static String date = "";
    public static String ordering = "schedule__starts_at_time";
    public static String chatAvatar = "http://doc.sunrisetest.site/media/1560327040458216_1560327010230.jpg";

    public static String ttt(){

        return max_price+" "+min_price+" "+schedule_time_before+" "+schedule_time_after+" "+date;
    }



}
