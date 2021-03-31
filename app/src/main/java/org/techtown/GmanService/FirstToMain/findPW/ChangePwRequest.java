package org.techtown.GmanService.FirstToMain.findPW;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePwRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/upDatepw.php";
    private Map<String,String> map;

    public ChangePwRequest(String Eid, String newpw, Response.Listener<String>listener){
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