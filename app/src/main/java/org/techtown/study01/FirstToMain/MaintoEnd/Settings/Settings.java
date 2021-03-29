package org.techtown.study01.FirstToMain.MaintoEnd.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.techtown.study01.FirstToMain.MaintoEnd.Special.ReviseDiary;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;

public class Settings extends Fragment {

    private ViewGroup viewGroup;
    private Button smokeReSettings, developerGive, opinion, review, version, logout;
    private Loading_Dialog loading_dialog;
    private SharedViewModel sharedViewModel;
    private Intent intent;
    private long cost, count;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadingStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.settings, container, false);


        loading_dialog.dismiss();

        setInit();

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

    }

    /**참조하기 */
    private void setInit() {
        smokeReSettings = viewGroup.findViewById(R.id.smokeReSettings);
        developerGive = viewGroup.findViewById(R.id.developerGive);
        opinion = viewGroup.findViewById(R.id.opinion);
        review = viewGroup.findViewById(R.id.review);
        version = viewGroup.findViewById(R.id.version);
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