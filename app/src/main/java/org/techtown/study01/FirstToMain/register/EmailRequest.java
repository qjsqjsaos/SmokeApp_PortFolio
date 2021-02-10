package org.techtown.study01.FirstToMain.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmailRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://qjsqjsaos.dothome.co.kr/emailCheck.php";
    private Map<String,String> map;

    public EmailRequest(String email, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("email", email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
