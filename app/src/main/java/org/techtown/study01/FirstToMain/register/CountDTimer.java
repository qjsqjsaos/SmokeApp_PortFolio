package org.techtown.study01.FirstToMain.register;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CountDTimer extends AppCompatActivity {


    public static String countDown() {
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int c_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int c_min = calendar.get(Calendar.MINUTE);
            int c_sec = calendar.get(Calendar.SECOND);

            Calendar baseCal = new GregorianCalendar(c_hour, c_min, c_sec);
            Calendar targetCal = new GregorianCalendar( 0, 0, 0);  //비교대상날짜

            long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000;
            long diffDays = diffSec / (24 * 60 * 60);

            targetCal.add(Calendar.DAY_OF_MONTH, (int) (-diffDays));

            int hourTime = (int) Math.floor((double) (diffSec / 3600));
            int minTime = (int) Math.floor((double) (((diffSec - (3600 * hourTime)) / 60)));
            int secTime = (int) Math.floor((double) (((diffSec - (3600 * hourTime)) - (60 * minTime))));

            String min = String.format("%02d", minTime);
            String sec = String.format("%02d", secTime);

            return min + ":" + sec ;

        }
    }
