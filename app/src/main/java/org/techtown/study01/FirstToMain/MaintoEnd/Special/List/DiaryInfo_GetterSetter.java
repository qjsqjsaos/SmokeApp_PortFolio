package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;


//다이어리 정보 게터세터 (어뎁터를 통해 각 아이템의 데이터를 담아두는 클래스)
public class DiaryInfo_GetterSetter {

    String R_title; //금연제목
    String R_writeDate; //금연 일기 쓴 날짜

    public DiaryInfo_GetterSetter(String r_title, String r_writeDate) { //생성자
        this.R_title = r_title;
        this.R_writeDate = r_writeDate;
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
}
