package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Calculate_Date;
import org.techtown.study01.FirstToMain.homeMain.CustomDialog;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class Frag2 extends Fragment {

    private SharedViewModel sharedViewModel;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false );

        textView = view.findViewById(R.id.textView8287);

       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long Longa) {
                DecimalFormat format = new DecimalFormat("###,###"); // 콤마 표시를 해준다(예 123123 => 123,123
                textView.setText("벌써 "+ format.format(Longa) +"일이 되었어요!");
            }
        });
    }
}
