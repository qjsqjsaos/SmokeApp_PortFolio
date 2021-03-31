package org.techtown.GmanService.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import org.techtown.GmanService.FirstToMain.R;

public class Loading_Dialog extends Dialog {

    public Loading_Dialog(Context context)
    {
        super(context);
        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading__dialog);

    }
}