package org.techtown.GmanService.FirstToMain.homeMain.ViewpagerFM;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Frag4_Request extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/update_goal.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public Frag4_Request(String id, String goal, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("goal", goal);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
