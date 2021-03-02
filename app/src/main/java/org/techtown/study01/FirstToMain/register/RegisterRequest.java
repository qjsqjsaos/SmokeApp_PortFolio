package org.techtown.study01.FirstToMain.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//볼리 라이브러리 활용

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/register.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public RegisterRequest(String id, String pw, String name, String email,
                           String datetime, long cigacount, double cigapay,
                           String goal, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();

        map.put("id", id);
        map.put("pw", pw);
        map.put("name", name);
        map.put("email", email);
        map.put("datetime", datetime);
        map.put("cigacount", cigacount+"");
        map.put("cigapay", cigapay+"");
        map.put("goal", goal);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
