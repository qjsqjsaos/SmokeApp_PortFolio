package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class delete_Diary_request extends StringRequest  {



    //서버 url 설정(php파일 연동)
    final static  private String URL="http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/delete_Diary_request.php";
    private Map<String,String> map;

    public delete_Diary_request(int num, String startdate, Response.Listener<String>listener){
        super(POST,URL,listener,null);

        map=new HashMap<>();
        map.put("num", num+"");
        map.put("startdate", startdate);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
