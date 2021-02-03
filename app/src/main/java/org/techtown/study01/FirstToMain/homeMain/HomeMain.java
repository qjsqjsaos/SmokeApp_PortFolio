package org.techtown.study01.FirstToMain.homeMain;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.techtown.study01.FirstToMain.R;

//홈 화면
public class HomeMain extends Fragment{

    ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        User_CardView user_cardView = viewGroup.findViewById(R.id.cardView);

        /** 02월 03일 마지막. 여기부터 다시시작하기*/

        return viewGroup;
    }

}