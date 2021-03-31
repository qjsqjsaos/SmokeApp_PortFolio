package org.techtown.GmanService.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.GmanService.FirstToMain.R;

public class Frag1 extends Fragment {

    public static TextView textView; //타이머 나타내기 위한 텍스트뷰
    private SharedViewModel sharedViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false ); //인플레이션하기
        textView = view.findViewById(R.id.textView847); //타이머 나타내기 위한 텍스트뷰 참조



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getstartDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String str) {
                textView.setText("오늘을 기준으로\n\n" + str + "시간 째 금연 중");
            }
        });
    }
}




