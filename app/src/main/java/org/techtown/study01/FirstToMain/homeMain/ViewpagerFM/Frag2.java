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
        sharedViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText("벌써 "+ s +"일이 되었어요!");
            }
        });
    }
}
