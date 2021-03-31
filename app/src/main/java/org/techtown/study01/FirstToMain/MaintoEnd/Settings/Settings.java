package org.techtown.study01.FirstToMain.MaintoEnd.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.techtown.study01.FirstToMain.MaintoEnd.Notice.Notice;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.ReviseDiary;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

public class Settings extends Fragment {

    private ViewGroup viewGroup;
    private Button smokeReSettings, developerGive, opinion, review, notice, logout;
    private Loading_Dialog loading_dialog;
    private SharedViewModel sharedViewModel;
    private Intent intent;
    private long cost, count;

    private AdView adView;
    private FrameLayout adContainerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadingStart();
    }

    /**애드몹 시작*/

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);

    }

    private AdSize getAdSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);
    }

    /**애드몹 끝*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.settings, container, false);


        loading_dialog.dismiss();

        setInit();

        // 애드 몹 초기화 //시작
        MobileAds.initialize(getActivity(), initializationStatus -> { });

        adContainerView = viewGroup.findViewById(R.id.ad_view_container4);
        adView = new AdView(getActivity());
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

        buttonListener();

        return viewGroup;

    }
    /**버튼 리스너*/
    private void buttonListener() {
        smokeReSettings.setOnClickListener(v -> {
            intent = new Intent(getActivity(), SmokeReSettings.class);
            intent.putExtra("cost", cost);
            intent.putExtra("count", count);
            startActivity(intent);
            Log.d("들어오냐", String.valueOf(cost));
            Log.d("들어오냐", String.valueOf(count));
        });

        opinion.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeABrL-1cL3iFHo86hdlULyw6KwvMhNvfNFR5A6kRAj_z0kWA/viewform?vc=0&c=0&w=1&flr=0&gxids=7628"));
            startActivity(intent);
        });

        notice.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), Notice.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            dialog();
        });

    }

    /**로그아웃하기 */

    private void goLogin() {
        Intent intent = new Intent(getContext(), Login.class);
        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putString("inputId", null);
        autoLogin.putString("inputPwd", null);
        autoLogin.putString("inputName", null);
        autoLogin.commit(); //커밋을 해야지 값이 저장된다.
        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    /**다이얼로그 띄우기 */

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false); //외부 클릭시 막아주기
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goLogin();
                    }
                });
        builder.show();
    }


    /**참조하기 */
    private void setInit() {
        smokeReSettings = viewGroup.findViewById(R.id.smokeReSettings);
        developerGive = viewGroup.findViewById(R.id.developerGive);
        opinion = viewGroup.findViewById(R.id.opinion);
        review = viewGroup.findViewById(R.id.review);
        notice = viewGroup.findViewById(R.id.notice);
        logout = viewGroup.findViewById(R.id.logout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loading_dialog.dismiss();
    }

    /**로딩창*/
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(getContext());
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //비용
        sharedViewModel.getLiveCost().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long along) {
                cost = along;
                Log.d("들어오냐2", String.valueOf(along));
            }
        });
        //담배 갯수
        sharedViewModel.getLiveCount().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long along) {
                count = along;
                Log.d("들어오냐2", String.valueOf(along));
            }
        });

    }
}