package org.techtown.study01.FirstToMain.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateDiaryTable_Check extends StringRequest  {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://ec2-3-35-9-74.ap-northeast-2.compute.amazonaws.com/CreateDiaryTable.php";
    private Map<String,String> map;

    public CreateDiaryTable_Check(int num,  Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("num", num+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
