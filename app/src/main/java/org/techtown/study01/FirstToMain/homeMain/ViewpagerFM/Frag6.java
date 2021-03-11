package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.study01.FirstToMain.R;


public class Frag6 extends Fragment {

    private LinearLayout ShareLayout; //공유하기 레이아웃

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_6, container, false );


        ShareLayout = view.findViewById(R.id.ShareLayout); //레이아웃 참조
        ShareLayout.setOnClickListener(new View.OnClickListener() {

            //친구와 공유하기
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=org.techtown.study01.FirstToMain");
                msg.putExtra(Intent.EXTRA_TITLE, "금연 솔루션 플랫폼 '그만'");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "중요한 건 중요한 건 실패가 아니라, 다시 도전하는 것입니다."));
            }
        });


        return view;

    }


}
