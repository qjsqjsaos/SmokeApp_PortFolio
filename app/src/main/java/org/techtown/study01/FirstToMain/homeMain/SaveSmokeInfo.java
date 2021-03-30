package org.techtown.study01.FirstToMain.homeMain;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SaveSmokeInfo extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/SaveSmokeInfo.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public SaveSmokeInfo(String datetime, long cigacount, long cigacost, String Eid, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("datetime", datetime);
        map.put("cigacount", cigacount + "");
        map.put("cigacost", cigacost + "");
        map.put("Eid",Eid);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
