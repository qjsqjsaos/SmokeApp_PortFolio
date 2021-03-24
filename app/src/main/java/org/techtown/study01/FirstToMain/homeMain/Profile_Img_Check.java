package org.techtown.study01.FirstToMain.homeMain;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Profile_Img_Check extends StringRequest  {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/profileimg.php";
    private Map<String,String> map;

    public Profile_Img_Check(String id, String profileimg, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("id", id);
        map.put("profileimg", profileimg);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
