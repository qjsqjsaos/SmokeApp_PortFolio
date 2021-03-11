package org.techtown.study01.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag4;

public class Purpose_Dialog extends Dialog {

    public static Dialog dialog; //dialog 객체
    public static Button purposeApply, cancelP;
    public static EditText inputGoal;


    public Purpose_Dialog(@NonNull Context context) {
        super(context);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.purpose);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT; //가로길이
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기

        purposeApply = dialog.findViewById(R.id.apply2); //금연 목적 지정 버튼
        cancelP = dialog.findViewById(R.id.cancelP); //취소 버튼
        inputGoal = dialog.findViewById(R.id.inputGoal); //목적 입력 에딧텍스트

        //새로운 목표가져와서 다이얼로그창에 자동으로 넣어주기
        String newGoal = Frag4.newGoalText.getText().toString();
        inputGoal.setText(newGoal);
    }

}
