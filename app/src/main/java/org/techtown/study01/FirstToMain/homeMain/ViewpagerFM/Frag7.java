package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.study01.FirstToMain.R;

import java.text.DecimalFormat;

public class Frag7 extends Fragment {

    private SharedViewModel sharedViewModel;
    public static ImageView rankImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_7, container, false );

        rankImg = view.findViewById(R.id.rankImg); //등급 이미지

       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.gethaelthSecond().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) { //1초면 1로 10초면 10으로 딱 맞춰서 넘어옴.
                //시간에 흐름에 따라 이미지 바꾸기
                if(259200 > aLong) { //금연 타이머가 시작되면
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.stone_hm); //돌멩이 이미지넣기
                }else if(aLong + 259200 < 2628000){ //3일 째 되는 날부터 1달째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.break_stone_hm); //깨진 돌멩이 이미지넣기
                }else if(aLong + 2628000 < 7884000) { //한달 째 되는 날부터 3달째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.bronzemedal_hm); //동메달 이미지 넣기
                }else if(aLong + 7884000 < 23650000) {//3달 째 되는 날부터 9달째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.sivermedal_hm); //은메달 이미지 넣기
                }else if(aLong + 23650000 < 157700000) {//9달 째 되는 날부터 5년째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.goldmedal_hm); //금메달 이미지 넣기
                }else if(aLong + 157700000 < 315400000) {//5년 째 되는 날부터 10년째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.diamond_hm); //다이아 이미지 넣기
                }else if(aLong + 315400000 < 473000000) {//10년 째 되는날부터 15년째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.siverstar_hm); //은별 이미지 넣기
                }else if(aLong + 473000000 < 630700000) {//15년 째 되는날부터 20년째 되는 날까지
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.goldstar_hm); //금별 이미지 넣기
                }else if(aLong >= 630700000) {//20년 째 되는날~
                    rankImg.setImageResource(0);
                    rankImg.setImageResource(R.drawable.lung_hm); //1등급 폐 이미지 넣기
                }

            }
        });
    }
}
