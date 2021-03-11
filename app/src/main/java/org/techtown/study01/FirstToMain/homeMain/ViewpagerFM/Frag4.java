package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.homeMain.Profile_Dialog;
import org.techtown.study01.FirstToMain.homeMain.Purpose_Dialog;

public class Frag4 extends Fragment {

    public static TextView newGoalText;
    private Loading_Dialog loading_dialog;

    private LinearLayout purpose; //리니어레이아웃 클릭 시 목적 변경 가능

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        newGoalText = view.findViewById(R.id.textView881); //텍스트뷰 참조

        purpose = (LinearLayout) view.findViewById(R.id.purpose);
        purpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Purpose_Dialog purpose_dialog = new Purpose_Dialog(getContext());
                purpose_dialog.purposeApply.setOnClickListener(new View.OnClickListener() { //금연 목적 지정 버튼 누를 떄
                    @Override
                    public void onClick(View v) {
                        loadingStart(); //로딩창 띄우기
                        //만약 처음에 글씨가 그대로라면 적용하기를 누를 때 디비 실행안하고 값 적용 => 첫번째
                        //하지만 목표가 처음 글씨와 다를 경우 디비실행하고 값적용 => 두번째
                        String goal = Purpose_Dialog.inputGoal.getText().toString(); //다이얼로그의 값 (변경하려는 목표값)
                        String PP = newGoalText.getText().toString(); //Frag4의 텍스트뷰 값 (기존 목표값)
                        loading_dialog.show(); //로딩창 띄우기
                        if (PP.equals(goal)) { //첫번째
                            Purpose_Dialog.dialog.dismiss(); //다이어로그닫기
                            loading_dialog.dismiss(); //로딩창 닫기
                        }
                        if (!PP.equals(goal)) { //두번째
                            upDateGoal(goal); //디비로 변경된 목표값 보내기기
                            Purpose_Dialog.dialog.dismiss(); //다이어로그 닫기
                        }
                    }
                });

                purpose_dialog.cancelP.setOnClickListener(new View.OnClickListener() { //취소 버튼 누를때
                    @Override
                    public void onClick(View v) {
                        Purpose_Dialog.dialog.dismiss(); //다이어로그 닫기
                    }
                });
            }
        });

        return view;

    }

    /** 디비로 금연 목표 정보 보내기*/
    private void upDateGoal(String goal) {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        newGoalText.setText(goal); //textView에다가 넣는다.
                        loading_dialog.cancel(); //디비에 저장하면 로딩창 끄기
                    } else {//실패
                        Toast.makeText(getContext(), "값이 저장되지 않았습니다. 인터넷을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "값이 저장되지 않았습니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Frag4_Request frag4_request = new Frag4_Request(HomeMain.id, goal, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag4_request);
        Log.d("골값보자", goal + "/" + HomeMain.id);

    }

    public void loadingStart() {
        loading_dialog = new Loading_Dialog(getContext());
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}
