package org.techtown.study01.FirstToMain.homeMain;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calculate_Date {

    public String WhatTimeIsItAll() { //전체 다
        //현재 시간을 나타내는 메서드
        long now = System.currentTimeMillis();

        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String getAll = simpleDateFormat.format(mDate); //스트링 형태로 현재 날짜 시간을 가져옴.

        return getAll;
    }


    public String WhatTimeIsItDate() { //날짜만
        //현재 시간을 나타내는 메서드
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getDay = simpleDateFormat.format(mDate); //스트링 형태로 현재 날짜를 가져옴.

        return getDay;
    }

    public String WhatTimeIsItTime() { //시간만
        //현재 시간을 나타내는 메서드
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String getTime = simpleDateFormat.format(mDate); //스트링 형태로 현재 시간을 가져옴.

        return getTime;
    }

    public long calDateBetweenAandB(String date1, String date2) throws ParseException //날짜 차이 구하기 "yyyy-mm-dd HH:mm" 이런 형식으로 넣어야함.
    {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);  //지정한날(금연 시작날)
            Date SecondDate = format.parse(date2); //현재 날짜

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = SecondDate.getTime() - FirstDate.getTime();
            long lastCalDate = calDate/10; //연산 후에는 0이 하나 더 추가되어, 이렇게 10으로 나누어 준다.
            Log.d("칼데이트", String.valueOf(FirstDate));
            Log.d("칼데이트", String.valueOf(SecondDate));
            Log.d("칼데이트", String.valueOf(calDate));
            Log.d("칼데이트", String.valueOf(lastCalDate));

            return lastCalDate;
    }

    public long calTimeBetweenAandB(String time1, String time2) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        Date d1 = f.parse(time1); //지정한 시간
        Date d2 = f.parse(time2); //현재 시간
        long diff = d2.getTime() - d1.getTime();
        long lastDiff = diff/10; //연산 후에는 0이 하나 더 추가되어, 이렇게 10으로 나누어 준다.
        Log.d("디프", String.valueOf(d2));
        Log.d("디프", String.valueOf(d1));
        Log.d("디프", String.valueOf(diff));
        Log.d("디프", String.valueOf(lastDiff));
        return lastDiff;
    }

}
