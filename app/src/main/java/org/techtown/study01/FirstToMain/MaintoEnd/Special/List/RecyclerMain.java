package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import org.techtown.study01.FirstToMain.R;

public class RecyclerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //리사이클러뷰에 레이아웃 매니저 설정하기
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter();

        //어뎁터에 넣을 데이터 넣어주기
        adapter.addItem(new DiaryInfo_GetterSetter("안녕", "1일차","2012-07-11"));


        //리사이클러뷰 어뎁터 설정으로 마무리
        recyclerView.setAdapter(adapter);
    }
}