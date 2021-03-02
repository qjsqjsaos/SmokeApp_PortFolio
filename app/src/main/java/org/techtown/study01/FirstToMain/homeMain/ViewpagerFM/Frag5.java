package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.study01.FirstToMain.R;

import java.text.DecimalFormat;


public class Frag5 extends Fragment {

    private SharedViewModel sharedViewModel;
    public static TextView textView5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_5, container, false );

       textView5 = view.findViewById(R.id.textView8133);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getLiveDataCount().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long Longa) {
                DecimalFormat format = new DecimalFormat("###,###,###,###,###,###,###,###"); // 콤마 표시를 해준다(예 123123 => 123,123
                textView5.setText(format.format(Longa) + "개비 가량 됩니다!");
            }
        });

    }
}
