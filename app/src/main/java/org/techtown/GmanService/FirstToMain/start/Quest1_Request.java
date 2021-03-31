package org.techtown.GmanService.FirstToMain.start;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Quest1_Request extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/Quest.php"; //호스팅 주소 + php
    private Map<String, String> map;

    public Quest1_Request(String Eid, long cigacount, long cigapay, String goal, int firstcheck, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("Eid", Eid);
        map.put("cigacount", cigacount + "");
        map.put("cigapay", cigapay + "");
        map.put("goal", goal);
        map.put("firstcheck", firstcheck + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
