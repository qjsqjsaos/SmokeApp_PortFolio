package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;
import org.techtown.study01.FirstToMain.login_fitstPage.LoginRequest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DiaryFrag extends Fragment {

    public static ImageView diaryImage; //일기 이미지뷰
    public static TextView diaryText; //일기 제목
    public static LinearLayout diaryFrag; //다이어리 프래그

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_frag, container, false);

        diaryImage = view.findViewById(R.id.diaryImage);
        diaryText = view.findViewById(R.id.diaryText);
        diaryFrag = view.findViewById(R.id.diaryFrag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            diaryImage.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0,0, view.getWidth(), view.getHeight()+200, 40);
                }
            });

            diaryImage.setClipToOutline(true);
        }

        viewDiary(); //다이어리 보기

        return view;
    }

    private void viewDiary() {
        diaryFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewDiary.class);
                intent.putExtra("title", Diary.viewtitle); //제목
                intent.putExtra("mainText", Diary.viewMaintText); //본문
                intent.putExtra("saveDate", Diary.dbDate); //날짜
                startActivity(intent); //ViewDiary로 보내기
            }
        });
    }
}