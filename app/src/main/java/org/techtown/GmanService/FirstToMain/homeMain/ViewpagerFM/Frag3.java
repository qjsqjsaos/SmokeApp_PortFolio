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

import java.text.DecimalFormat;


public class Frag3 extends Fragment {

    private SharedViewModel sharedViewModel;
    public static TextView textView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false );

        textView3 = view.findViewById(R.id.textView5848);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getLiveDataCost().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double Longa) {
                DecimalFormat format = new DecimalFormat("###,###,###,###,###,###,###,###.##"); // 콤마 표시를 해준다(예 123123 => 123,123
                textView3.setText(format.format(Longa) + "원");
            }
        });

    }
}
