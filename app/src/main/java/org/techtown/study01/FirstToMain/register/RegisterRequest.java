package org.techtown.study01.FirstToMain.register;

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



    public RegisterRequest(String id, String pw, String name, String email, String phone, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("id", id);
        map.put("pw", pw);
        map.put("name", name);
        map.put("email", email);
        map.put("phone", phone);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
