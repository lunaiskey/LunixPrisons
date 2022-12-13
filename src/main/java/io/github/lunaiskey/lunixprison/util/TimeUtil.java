package io.github.lunaiskey.lunixprison.util;

public class TimeUtil {

    public static String countdown(long time) {
        long currentTime = System.currentTimeMillis();
        long difference = time - currentTime;

        int seconds = (int) (difference / 1000) % 60 ;
        int minutes = (int) ((difference / (1000*60)) % 60);
        int hours   = (int) ((difference / (1000*60*60)) % 24);

        return hours+"h "+minutes+"m "+seconds+"s";
    }

    public static String parseTime(long time) {
        int seconds = (int) time % 60;
        int minutes = (int) (time / 60) % 60;
        int hours = (int) ((time / 60)/60);

        String hoursStr = hours > 0 ? hours +"h " : "";
        String minutesStr = minutes > 0 ? minutes +"m " : "";
        String secondStr;
        if (seconds <= 0 && hours > 0 || minutes > 0) {
            secondStr = "";
        } else {
            secondStr = seconds+"s";
        }
        return hoursStr+minutesStr+secondStr;
    }
}
