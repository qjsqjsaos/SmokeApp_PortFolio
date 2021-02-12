package org.techtown.study01.FirstToMain.findPW;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePw extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://qjsqjsaos.dothome.co.kr/upDatepw.php";
    private Map<String,String> map;

    public ChangePw(String Eid, String newpw, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("Eid", Eid);
        map.put("newpw", newpw);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}