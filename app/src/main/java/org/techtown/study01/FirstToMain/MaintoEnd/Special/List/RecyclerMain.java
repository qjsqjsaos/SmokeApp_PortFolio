package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.DiaryFrag;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.ViewDiary;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.delete_Diary_request;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.getDiaryDate_Request;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.getDiaryInfo_Request;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;

import java.util.ArrayList;

public class RecyclerMain extends AppCompatActivity {


    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<DiaryInfo_GetterSetter> item = new ArrayList<>();
    private Loading_Dialog loading_dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);
        loadingStart();//로딩창

        recyclerView = findViewById(R.id.recyclerView);

        //리사이클러뷰에 레이아웃 매니저 설정하기
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter();
        getDairyAllDate(); //일기 리스트 생성

        //리사이클러뷰 클릭시 액션
        adapter.setOnDiaryItemClickListener(new OnDiaryItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, View view, int position) {
                DiaryInfo_GetterSetter item = adapter.getItem(position);
                String rDate = item.R_writeDate; //클릭한 뷰 날짜정보를 가져온다.
                Log.d("리사이클러뷰", rDate);
                getDairyInfoRecyclerView(rDate); //보내서 디비에서 정보를 가져온다.
            }
        });

        loading_dialog.dismiss(); //로딩창끄기

    }

    /** 날짜를 통해 제목 정보 가져오기*/
    private void getDairyInfo(String date) {

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String title = jsonObject.getString("title"); //타이틀 가져오기

                        item.add(new DiaryInfo_GetterSetter(title, date));
                        //리사이클러뷰 어뎁터 설정으로 마무리
                        adapter.setItems(item);
                        recyclerView.setAdapter(adapter);

                        Log.d("베이베베이베2", title);
                        Log.d("베이베베이베2", date);
                        loading_dialog.dismiss(); //로딩창끄기

                    } else {//실패
                        Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        loading_dialog.dismiss(); //로딩창끄기
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    loading_dialog.dismiss(); //로딩창끄기
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창끄기
                }
            };

            getDiaryInfo_Request getDiaryInfo_request = new getDiaryInfo_Request(HomeMain.num, date, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplication());
            queue.add(getDiaryInfo_request);

        }


    /** 날짜를 통해 타이틀과 내용 정보 가져오기 (리사이클러뷰용)*/
    private void getDairyInfoRecyclerView(String date) {

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    String title = jsonObject.getString("title"); //타이틀 가오기
                    String mainText = jsonObject.getString("maintext"); //내용 가져오기
                    //RecyclerMain으로 이동,
                   Intent intent = new Intent(getApplication(), ViewDiary.class);
                   intent.putExtra("Rtitle", title);
                   intent.putExtra("RmainText", mainText);
                   intent.putExtra("Rdate", date);
                   startActivity(intent);
                    Log.d("마지막리사이클", title);
                    Log.d("마지막리사이클", mainText);

                } else {//실패
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        getDiaryInfo_Request getDiaryInfo_request = new getDiaryInfo_Request(HomeMain.num, date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(getDiaryInfo_request);

    }

    /** 고유넘버 num을 통해 모든 날짜 컬럼 가져오기*/
    private void getDairyAllDate() {

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    int length = jsonObject.length()-1;
                    for(int i = 0; i <= length; i++) { //있는 수만큼 반복문
                        String date = jsonObject.getString(String.valueOf(i));
                        Log.d("베이베베이베", String.valueOf(jsonObject.length()));
                        Log.d("베이베베이베", date);
                        //모든 컬럼날짜 전달
                        getDairyInfo(date);
                    }

                } else {//실패
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창끄기
                }


            } catch (JSONException e) {
                e.printStackTrace();
                loading_dialog.dismiss(); //로딩창끄기
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                loading_dialog.dismiss(); //로딩창끄기
            }
        };

        getDiaryDate_Request getDiaryDate_request = new getDiaryDate_Request(HomeMain.num, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(getDiaryDate_request);

    }
    /**
     * 로딩창
     */
    public void loadingStart() {
        loading_dialog = new Loading_Dialog(RecyclerMain.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }

}