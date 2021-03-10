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
            pgb11, pgb12, pgb13, pgb14, pgb15, pgb16, pgb17, pgb18, pgb19;

    private SharedViewModel sharedViewModel; //뷰모델 (데이터 받기)


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.health_check, container, false);

        TextView textView = viewGroup.findViewById(R.id.textView18);

        findProgress(); //프로그레스바 참조하기

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
                pgb1.setProgress(Math.toIntExact(Longa));
                pgb1.setMax(1200); //20분

                pgb2.setProgress(Math.toIntExact(Longa));
                pgb2.setMax(7200); //2시간

                pgb3.setProgress(Math.toIntExact(Longa));
                pgb3.setMax(28800); //8시간

                pgb4.setProgress(Math.toIntExact(Longa));
                pgb4.setMax(64800); //18시간

                pgb5.setProgress(Math.toIntExact(Longa));
                pgb5.setMax(172800); //2일

                pgb6.setProgress(Math.toIntExact(Longa));
                pgb6.setMax(259200); //3일

                pgb7.setProgress(Math.toIntExact(Longa));
                pgb7.setMax(345600); //4일

                pgb8.setProgress(Math.toIntExact(Longa));
                pgb8.setMax(604800); //1주일

                pgb9.setProgress(Math.toIntExact(Longa));
                pgb9.setMax(1210000); //2주일

                pgb10.setProgress(Math.toIntExact(Longa));
                pgb10.setMax(2628000); //1개월

                pgb11.setProgress(Math.toIntExact(Longa));
                pgb11.setMax(5256000); //2개월

                pgb12.setProgress(Math.toIntExact(Longa));
                pgb12.setMax(7884000); //3개월

                pgb13.setProgress(Math.toIntExact(Longa));
                pgb13.setMax(15770000); //6개월

                pgb14.setProgress(Math.toIntExact(Longa));
                pgb14.setMax(23650000); //9개월

                pgb15.setProgress(Math.toIntExact(Longa));
                pgb15.setMax(31540000); //1년

                pgb16.setProgress(Math.toIntExact(Longa));
                pgb16.setMax(157700000); //5년

                pgb17.setProgress(Math.toIntExact(Longa));
                pgb17.setMax(315400000); //10년

                pgb18.setProgress(Math.toIntExact(Longa));
                pgb18.setMax(473000000); //15년

                pgb19.setProgress(Math.toIntExact(Longa));
                pgb19.setMax(630700000); //20년
            }
        });
    }


    /**프로그레스바 참조하기 */
    private void findProgress() {
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
    }


}