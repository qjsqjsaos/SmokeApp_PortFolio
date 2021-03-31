package org.techtown.GmanService.FirstToMain.MaintoEnd.Special.List;


import android.content.Context;
import android.net.Uri;

//다이어리 정보 게터세터 (어뎁터를 통해 각 아이템의 데이터를 담아두는 클래스)
public class DiaryInfo_GetterSetter {

    private String R_title; //금연제목
    private String R_writeDate; //금연 일기 쓴 날짜
    private Uri R_uri; // 일기 사진
    private Context context;

    public DiaryInfo_GetterSetter(String r_title, String r_writeDate, Uri r_uri, Context context) { //생성자
        this.R_title = r_title;
        this.R_writeDate = r_writeDate;
        this.R_uri = r_uri;
        this.context = context;
    }

    public String getR_title() {
        return R_title;
    }

    public void setR_title(String r_title) {
        R_title = r_title;
    }

    public String getR_writeDate() {
        return R_writeDate;
    }

    public void setR_writeDate(String r_writeDate) {
        R_writeDate = r_writeDate;
    }

    public Uri getR_uri() {
        return R_uri;
    }

    public void setR_uri(Uri r_uri) {
        R_uri = r_uri;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

