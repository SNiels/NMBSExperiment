package be.howest.nmct.android.nmbsbruggekortrijk.models;

import java.util.Date;

/**
 * Created by Niels on 20/02/14.
 */
public class TrainAction {
    private int delay;
    private Station station;
    private int platform;
    private boolean isNormalPlatform;
    private Station direction;
    private Date time;

    /*
    http://stackoverflow.com/questions/17432735/convert-unix-time-stamp-to-date-in-java
    long unixSeconds = 1372339860;
    Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
    sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
    String formattedDate = sdf.format(date);
    System.out.println(formattedDate);
     */
}
