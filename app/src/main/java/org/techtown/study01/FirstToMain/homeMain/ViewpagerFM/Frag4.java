package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techtown.study01.FirstToMain.R;

public class Frag4 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_4, container, false );

        setInit(view);

        return view;

    }

    private void setInit(View _view) {
        TextView textView = _view.findViewById(R.id.textView); //여기에 각자 텍스트 뷰 참조 가능
    }
}
