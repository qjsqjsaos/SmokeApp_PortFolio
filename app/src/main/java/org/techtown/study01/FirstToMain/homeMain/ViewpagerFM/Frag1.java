package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.study01.FirstToMain.R;

public class Frag1 extends Fragment {
    String timer;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false );


        setInit(view);

        if(timer != null) {
            Bundle bundle = getArguments();
            timer = bundle.getString("setTimer");
            textView.setText("오늘을 기준으로\n\n" + timer + "시간 째 금연 중");
        }

        return view;

    }

    private void setInit(View _view) {
            textView = _view.findViewById(R.id.textView847); //여기에 각자 텍스트 뷰 참조 가능

        }
    }
