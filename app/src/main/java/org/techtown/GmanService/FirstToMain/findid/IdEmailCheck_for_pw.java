package org.techtown.GmanService.FirstToMain.findid;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdEmailCheck_for_pw extends StringRequest {



    //서버 url 설정(php파일 연동)
    final static  private String URL="http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com//Id_Email_check_for_pw.php";
    private Map<String,String> map;

    public IdEmailCheck_for_pw(String id, String email, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("email", email);
        map.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}