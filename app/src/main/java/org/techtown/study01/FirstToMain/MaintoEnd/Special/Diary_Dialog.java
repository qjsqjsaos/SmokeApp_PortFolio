package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag4;

public class Diary_Dialog extends Dialog {

    public static Dialog dialog; //dialog 객체
    public static EditText title_diary, mainText_diary;
    public static Button inputImage, cancel_btn_diary, saveDiary;


    public Diary_Dialog(@NonNull Context context) {
        super(context);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.diary_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        params.width = (int) (dm.widthPixels * 0.8); //가로길이
        params.height = (int) (dm.heightPixels * 0.75); //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기

        title_diary = dialog.findViewById(R.id.title_diary); //일기 제목
        mainText_diary = dialog.findViewById(R.id.mainText); //일기 본문
        inputImage = dialog.findViewById(R.id.inputImage); //이미지 첨부 버튼
        cancel_btn_diary = dialog.findViewById(R.id.cancel_btn_diary); //일기 취소 버튼
        saveDiary = dialog.findViewById(R.id.saveDiary); //일기 작성완료 버튼
    }

}
