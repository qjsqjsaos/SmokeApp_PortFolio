package org.techtown.study01.FirstToMain.MaintoEnd.HomeMain;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HealthCheck extends Fragment {

    private ViewGroup viewGroup;
    public static ProgressBar pgb1, pgb2, pgb3, pgb4, pgb5, pgb6, pgb7, pgb8, pgb9, pgb10,
            pgb11, pgb12, pgb13, pgb14, pgb15, pgb16, pgb17, pgb18, pgb19; //프로그레스바

    public static ImageView i1, i2, i3, i4, i5, i6, i7, i8, i9, i10,
            i11, i12, i13, i14, i15, i16, i17, i18, i19; //이미지뷰

    private SharedViewModel sharedViewModel; //뷰모델 (데이터 받기)


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.health_check, container, false);

        TextView textView = viewGroup.findViewById(R.id.textView18);

        findProgressandImageView(); //프로그레스바와 이미지뷰 참조하기

        return viewGroup;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.gethaelthSecond().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Long Longa) { //넘어온 Longa + 00을 해주고, setMax에 넘겨주어 값을 맞춘다.
                Log.d("롱가", String.valueOf(Longa));

                healthCheck(Longa); //프로그레스바와 이미지뷰 상황에 맞게 실행

            }
        });
    }

    private void healthCheck(Long Longa) {

        long Longb = Longa * 1L; //넘어온 값 하나의 변수로 묶기

        pgb1.setProgress((int) Longb);
        pgb1.setMax(1200); //20분

        if(Longa >= 1200 && Longa < 630700001){// 20분을 같거나 넘기면
            i1.setImageResource(R.drawable.stone_hc); //이미지 넣기
        }

        pgb2.setProgress((int) Longb);
        pgb2.setMax(7200); //2시간

        if(Longa >= 7200 && Longa < 630700001){// 2시간을 같거나 넘기면
            i2.setImageResource(R.drawable.stone_hc); //이미지 넣기  //이하 생력
        }

        pgb3.setProgress((int) Longb);
        pgb3.setMax(28800); //8시간

        if(Longa >= 28800 && Longa < 630700001){// 8시간을 같거나 넘기면
            i3.setImageResource(R.drawable.stone_hc);
        }

        pgb4.setProgress((int) Longb);
        pgb4.setMax(64800); //18시간

        if(Longa >= 64800 && Longa < 630700001){// 18시간을 같거나 넘기면 //이하생략
            i4.setImageResource(R.drawable.stone_hc);
        }

        pgb5.setProgress((int) Longb);
        pgb5.setMax(172800); //2일

        if(Longa >= 172800 && Longa < 630700001){
            i5.setImageResource(R.drawable.stone_hc);
        }

        pgb6.setProgress((int) Longb);
        pgb6.setMax(259200); //3일

        if(Longa >= 259200 && Longa < 630700001){
            i6.setImageResource(R.drawable.break_stone_hc);
        }

        pgb7.setProgress((int) Longb);
        pgb7.setMax(345600); //4일

        if(Longa >= 345600 && Longa < 630700001){
            i7.setImageResource(R.drawable.break_stone_hc);
        }

        pgb8.setProgress((int) Longb);
        pgb8.setMax(604800); //1주일

        if(Longa >= 604800 && Longa < 630700001){
            i8.setImageResource(R.drawable.break_stone_hc);
        }

        pgb9.setProgress((int) Longb);
        pgb9.setMax(1210000); //2주일

        if(Longa >= 1210000 && Longa < 630700001){
            i9.setImageResource(R.drawable.break_stone_hc);
        }

        pgb10.setProgress((int) Longb);
        pgb10.setMax(2628000); //1개월

        if(Longa >= 2628000 && Longa < 630700001){
            i10.setImageResource(R.drawable.bronzemedal_hc);
        }


        pgb11.setProgress((int) Longb);
        pgb11.setMax(5256000); //2개월

        if(Longa >= 5256000 && Longa < 630700001){
            i11.setImageResource(R.drawable.bronzemedal_hc);
        }

        pgb12.setProgress((int) Longb);
        pgb12.setMax(7884000); //3개월

        if(Longa >= 7884000 && Longa < 630700001){
            i12.setImageResource(R.drawable.sivermedal_hc);
        }

        pgb13.setProgress((int) Longb);
        pgb13.setMax(15770000); //6개월

        if(Longa >= 15770000 && Longa < 630700001){
            i13.setImageResource(R.drawable.sivermedal_hc);
        }

        pgb14.setProgress((int) Longb);
        pgb14.setMax(23650000); //9개월

        if(Longa >= 23650000 && Longa < 630700001){
            i14.setImageResource(R.drawable.goldmedal_hc);
        }


        pgb15.setProgress((int) Longb);
        pgb15.setMax(31540000); //1년

        if(Longa >= 31540000 && Longa < 630700001){
            i15.setImageResource(R.drawable.goldmedal_hc);
        }

        pgb16.setProgress((int) Longb);
        pgb16.setMax(157700000); //5년

        if(Longa >= 157700000 && Longa < 630700001){
            i16.setImageResource(R.drawable.diamond_hc);
        }

        pgb17.setProgress((int) Longb);
        pgb17.setMax(315400000); //10년

        if(Longa >= 315400000 && Longa < 630700001){
            i17.setImageResource(R.drawable.siverstar_hc);
        }

        pgb18.setProgress((int) Longb);
        pgb18.setMax(473000000); //15년

        if(Longa >= 473000000 && Longa < 630700001){
            i18.setImageResource(R.drawable.goldstar_hc);
        }

        pgb19.setProgress((int) Longb);
        pgb19.setMax(630700000); //20년

        if(Longa >= 630700000 && Longa < 630700001){
            i19.setImageResource(R.drawable.lung_hc);
        }

        if(Longa >= 630700002) { //시간이 어느정도가면 setProgress에 다 안담기기 때문에 임의로 setMax를 다 채워준다.
            //프로그레스바 꽉채우기
            pgb1.setProgress(1200);
            pgb2.setProgress(7200);
            pgb3.setProgress(28800);
            pgb4.setProgress(64800);
            pgb5.setProgress(172800);
            pgb6.setProgress(259200);
            pgb7.setProgress(345600);
            pgb8.setProgress(604800);
            pgb9.setProgress(1210000);
            pgb10.setProgress(2628000);
            pgb11.setProgress(5256000);
            pgb12.setProgress(7884000);
            pgb13.setProgress(15770000);
            pgb14.setProgress(23650000);
            pgb15.setProgress(31540000);
            pgb16.setProgress(157700000);
            pgb17.setProgress(315400000);
            pgb18.setProgress(473000000);
            pgb19.setProgress(630700000);

            //이미지 다 넣어놓기
            i1.setImageResource(R.drawable.stone_hc); //이미지 넣기
            i2.setImageResource(R.drawable.stone_hc); //이미지 넣기  //이하 생력
            i3.setImageResource(R.drawable.stone_hc);
            i4.setImageResource(R.drawable.stone_hc);
            i5.setImageResource(R.drawable.stone_hc);
            i6.setImageResource(R.drawable.break_stone_hc);
            i7.setImageResource(R.drawable.break_stone_hc);
            i8.setImageResource(R.drawable.break_stone_hc);
            i9.setImageResource(R.drawable.break_stone_hc);
            i10.setImageResource(R.drawable.bronzemedal_hc);
            i11.setImageResource(R.drawable.bronzemedal_hc);
            i12.setImageResource(R.drawable.sivermedal_hc);
            i13.setImageResource(R.drawable.sivermedal_hc);
            i14.setImageResource(R.drawable.goldmedal_hc);
            i15.setImageResource(R.drawable.goldmedal_hc);
            i16.setImageResource(R.drawable.diamond_hc);
            i17.setImageResource(R.drawable.siverstar_hc);
            i18.setImageResource(R.drawable.goldstar_hc);
            i19.setImageResource(R.drawable.lung_hc);
        }
    }


    /**프로그레스바와 이미지뷰 참조하기 */
    private void findProgressandImageView() {

        //프로그래스 바
        pgb1 = viewGroup.findViewById(R.id.progressbar1); //프로그레스바 1
        pgb2 = viewGroup.findViewById(R.id.progressbar2); //프로그레스바 2
        pgb3 = viewGroup.findViewById(R.id.progressbar3); //프로그레스바 3
        pgb4 = viewGroup.findViewById(R.id.progressbar4); //프로그레스바 4
        pgb5 = viewGroup.findViewById(R.id.progressbar5); //프로그레스바 5
        pgb6 = viewGroup.findViewById(R.id.progressbar6); //프로그레스바 6
        pgb7 = viewGroup.findViewById(R.id.progressbar7); //프로그레스바 7
        pgb8 = viewGroup.findViewById(R.id.progressbar8); //프로그레스바 8
        pgb9 = viewGroup.findViewById(R.id.progressbar9); //프로그레스바 9
        pgb10 = viewGroup.findViewById(R.id.progressbar10); //프로그레스바 10
        pgb11 = viewGroup.findViewById(R.id.progressbar11); //프로그레스바 11
        pgb12 = viewGroup.findViewById(R.id.progressbar12); //프로그레스바 12
        pgb13 = viewGroup.findViewById(R.id.progressbar13); //프로그레스바 13
        pgb14 = viewGroup.findViewById(R.id.progressbar14); //프로그레스바 14
        pgb15 = viewGroup.findViewById(R.id.progressbar15); //프로그레스바 15
        pgb16 = viewGroup.findViewById(R.id.progressbar16); //프로그레스바 16
        pgb17 = viewGroup.findViewById(R.id.progressbar17); //프로그레스바 17
        pgb18 = viewGroup.findViewById(R.id.progressbar18); //프로그레스바 18
        pgb19 = viewGroup.findViewById(R.id.progressbar19); //프로그레스바 19

        //이미지 뷰

        i1 = viewGroup.findViewById(R.id.i1); //이미지뷰 1
        i2 = viewGroup.findViewById(R.id.i2); //이미지뷰 2
        i3 = viewGroup.findViewById(R.id.i3); //이미지뷰 3
        i4 = viewGroup.findViewById(R.id.i4); //이미지뷰 4
        i5 = viewGroup.findViewById(R.id.i5); //이미지뷰 5
        i6 = viewGroup.findViewById(R.id.i6); //이미지뷰 6
        i7 = viewGroup.findViewById(R.id.i7); //이미지뷰 7
        i8 = viewGroup.findViewById(R.id.i8); //이미지뷰 8
        i9 = viewGroup.findViewById(R.id.i9); //이미지뷰 9
        i10 = viewGroup.findViewById(R.id.i10); //이미지뷰 10
        i11 = viewGroup.findViewById(R.id.i11); //이미지뷰 11
        i12 = viewGroup.findViewById(R.id.i12); //이미지뷰 12
        i13 = viewGroup.findViewById(R.id.i13); //이미지뷰 13
        i14 = viewGroup.findViewById(R.id.i14); //이미지뷰 14
        i15 = viewGroup.findViewById(R.id.i15); //이미지뷰 15
        i16 = viewGroup.findViewById(R.id.i16); //이미지뷰 16
        i17 = viewGroup.findViewById(R.id.i17); //이미지뷰 17
        i18 = viewGroup.findViewById(R.id.i18); //이미지뷰 18
        i19 = viewGroup.findViewById(R.id.i19); //이미지뷰 19
    }


}