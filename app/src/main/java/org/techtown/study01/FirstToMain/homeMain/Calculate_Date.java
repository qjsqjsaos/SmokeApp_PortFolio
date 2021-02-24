package org.techtown.study01.FirstToMain.homeMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calculate_Date { //날짜 차이 구하기

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String getTime = simpleDateFormat.format(mDate); //스트링 형태로 현재 시간을 가져옴.

        return getTime;
    }

    public long calDateBetweenAandB(String date1, String date2) //날짜 차이 구하기 "yyyy-mm-dd HH:mm" 이런 형식으로 넣어야함.
    {
        long calDateDays = 0;

        try { // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);  //지정한날(금연 시작날)
            Date SecondDate = format.parse(date2); //현재 날짜

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays); //두 날짜 차이


        } catch (ParseException e) {
            // 예외 처리
        }

        return calDateDays;
    }

    public String calTimeBetweenAandB(String time1, String time2)
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(time1);
            d2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        String lastDiff = Math.abs(diffHours) + ":" + Math.abs(diffMinutes); //Math.abs는 절댓값이다.
        return lastDiff;
    }

}
