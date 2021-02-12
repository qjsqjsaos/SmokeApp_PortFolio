package org.techtown.study01.FirstToMain.findid;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindId_Pw_Check extends StringRequest  {



    //서버 url 설정(php파일 연동)
    final static  private String URL="http://qjsqjsaos.dothome.co.kr/FindId_Pw.php";
    private Map<String,String> map;

    public FindId_Pw_Check(String email, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("email", email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
