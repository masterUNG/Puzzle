package net.galliliu.jigsawpuzzle.presenter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by galliliu on 16/5/28.
 */
public class TimeUtil {
    public static String formatTime(long time) {
        String timeStr = "";
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = (time % 3600) % 60;

        timeStr += (hours < 10 ? ("0" + hours) : hours) + ":";
        timeStr += (minutes < 10 ? ("0" + minutes) : minutes) + ":";
        timeStr += (seconds < 10 ? ("0" + seconds) : seconds);

        return timeStr;
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
