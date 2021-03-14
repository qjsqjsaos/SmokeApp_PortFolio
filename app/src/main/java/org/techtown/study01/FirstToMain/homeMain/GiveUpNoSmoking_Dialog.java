package org.techtown.study01.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.techtown.study01.FirstToMain.R;

import java.text.ParseException;

public class GiveUpNoSmoking_Dialog extends Dialog {

    public static Dialog dialog; //dialog 객체
    public static Button NSYES, NSNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public GiveUpNoSmoking_Dialog(@NonNull Context context) {
        super(context);





        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.giveup_nosmoke);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        //비율에 따라 나타내기
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        params.width = (int) (dm.widthPixels * 0.8); //가로길이
        params.height = (int) (dm.heightPixels * 0.95); //세로길이

        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params); //적용하기기
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기



       NSYES = dialog.findViewById(R.id.retry_btn); //다시도전버튼
        NSNO = dialog.findViewById(R.id.cancel_btn846); //금연 그만하기 버튼

    }

}
