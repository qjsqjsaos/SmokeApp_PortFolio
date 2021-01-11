package org.techtown.study01;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//볼리 라이브러리 활용

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://qjsqjsaos.dothome.co.kr/Register.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public RegisterRequest(String userID, String userPassword, String userBirthday, String userNickname, int userPhone, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userBirthday", userBirthday);//인트형이니 가라식으로 스트링형태를 만들어 주기 위해 앞에 ""을 붙임.
        map.put("userNickname", userNickname);
        map.put("userPhone", userPhone + "");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
