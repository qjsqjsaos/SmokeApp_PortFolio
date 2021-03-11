package org.techtown.study01.FirstToMain.homeMain;

import java.util.ArrayList;

public class WiseSay_List extends ArrayList {

    public static ArrayList<String> wiseSay;

    //명언 모음 메서드
    public String WiseArray(int randomNumber){

        wiseSay = new ArrayList<>(); //배열리스트를 작성한다.
        int i = 0;

        /**명언 리스트 1000개*/
        String S = "W('안녕');";
                for(i=0; i<1000; i++){
                    System.out.println(S);
                }



        ///번호를 랜덤으로 받고,
        String WSValue = wiseSay.get(randomNumber);

        //그 번호에 맞게 명언을 리턴한다.
        return WSValue;
    }

    /** 명언 메서드 **/
    private void W(String w) {
        wiseSay.add(w);
        return;
    }

}
