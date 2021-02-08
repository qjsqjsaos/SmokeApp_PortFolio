package org.techtown.study01.FirstToMain.findid;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindId_Check extends StringRequest  {

    // TODO: 2021-02-08 php 코드 이용에서 JSON 실행하기
    // TODO: 2021-02-08 로그인 찾기 기능 , 비밀 번호 찾기 기능.
    // TODO: 2021-02-08 네비 아이콘 확인
    // TODO: 2021-02-08 각 화면 만들기


        //서버 url 설정(php파일 연동)
        final static  private String URL="http://qjsqjsaos.dothome.co.kr/FindId.php";
        private Map<String,String> map;

        public FindId_Check(String id, String email, Response.Listener<String>listener){
            super(Request.Method.POST,URL,listener,null);

            map=new HashMap<>();
            map.put("id", id);
            map.put("email", email);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }
