package org.techtown.study01.FirstToMain.Etc;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import org.techtown.study01.FirstToMain.R;

public class Loading_ProgressDialog extends Dialog {
    public Loading_ProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 다이얼 로그 제목을 안보이게...
        setContentView(R.layout.dialog_image);
        setCancelable(false); //주변을 눌러도 화면이 닫히지 않게 하는 기능
    }
}
