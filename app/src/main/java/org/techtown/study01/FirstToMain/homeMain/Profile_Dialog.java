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

public class Profile_Dialog extends Dialog {

    public static Dialog dialog; //dialog 객체
    public static Button apply, cancelprofile;

    public static EditText changedName; //이름바꾸기 에딧텍스트

    public static String newName; //새로운 이름 입력을 가져온 것 //null값 방지 Home.name 넣어주기

    //프로필 변경하기

    public Profile_Dialog(@NonNull Context context) {
        super(context);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profile_card);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 600; //가로길이
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기


        changedName = dialog.findViewById(R.id.editchangeName); //이름 바꾸기 텍스트
        apply = dialog.findViewById(R.id.changeApply); //적용하기
        cancelprofile = dialog.findViewById(R.id.cancel_profile); //취소

        changedName.setText(HomeMain.name); //이름 입력란에 원래 이름 넣기(바뀌기 전 이름)
        newName = changedName.getText().toString(); //새로바꾼이름

    }

}
