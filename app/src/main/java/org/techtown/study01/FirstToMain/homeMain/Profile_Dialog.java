package org.techtown.study01.FirstToMain.homeMain;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;


import org.techtown.study01.FirstToMain.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class Profile_Dialog extends Dialog {

    private CircleImageView profileImage; //프로필 이미지
    public static Dialog dialog; //dialog 객체
    public static Button apply, cancelprofile;

    public static EditText changedName; //이름바꾸기 에딧텍스트

    private Button change_btn; //프로필 변경 버튼


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

        profileImage = dialog.findViewById(R.id.circleImageView); //프로필 이미지
        changedName = dialog.findViewById(R.id.editchangeName); //이름 바꾸기 텍스트
        apply = dialog.findViewById(R.id.changeApply); //적용하기
        cancelprofile = dialog.findViewById(R.id.cancel_profile); //취소
        change_btn = dialog.findViewById(R.id.changeProfile); //프로필 이미지 변경 버튼

        String NN = HomeMain.nameView.getText().toString(); //원래이름 가져오기
        Profile_Dialog.changedName.setText(NN); //이름 입력란에 원래 이름 넣기(바뀌기 전 이름)
    }

}
