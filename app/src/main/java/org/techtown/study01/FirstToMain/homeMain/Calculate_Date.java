package org.techtown.study01.FirstToMain.homeMain;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public long calDateBetweenAandB(String another) throws ParseException //날짜 차이 구하기 "yyyy-mm-dd HH:mm" 이런 형식으로 넣어야함.
    {   //another는 지정날짜

        //데이트포맷(일수로 구할거니깐 dd까지만 있으면됨)
        SimpleDateFormat todaySdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        //한국기준 날짜
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        todaySdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String todayDate = todaySdf.format(date);
        //오늘 타임스탬프(데이트포맷으로 저장했다고 치고 그걸 타임스탬프로 바꿔보는 작업)
        long todayTimestamp = todaySdf.parse(todayDate).getTime();
        Date date2 = new Date(todayTimestamp);
        String todayDate2 = todaySdf.format(date2);

        //지정날짜
        String differentDate = another;
        long nextdayTimestamp = todaySdf.parse(differentDate).getTime();

        long difference = todayTimestamp- nextdayTimestamp;
        long last_diff = difference/10;
        System.out.println("오늘날짜 => "+todayDate2);
        System.out.println("다른날짜 => "+differentDate);
        System.out.println("differentTimestamp 타임스탬프=> "+todayTimestamp);
        System.out.println("todayTimestamp 타임스탬프=> "+todayTimestamp);
        System.out.println("일수차=> "  +  difference/ (24*60*60*1000));


        Log.d("알려줘", String.valueOf(difference));
        Log.d("알려줘", String.valueOf(last_diff));
        Log.d("알려줘", todayDate2);
        Log.d("알려줘", differentDate);

        return difference;
    }

    public long calTimeBetweenAandB(String time1, String time2) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        Date d1 = f.parse(time1); //지정한 시간
        Date d2 = f.parse(time2); //현재 시간
        long diff = d2.getTime() - d1.getTime();
        Log.d("디프", String.valueOf(d2));
        Log.d("디프", String.valueOf(d1));
        Log.d("디프", String.valueOf(diff));
        return diff;
    }

}
