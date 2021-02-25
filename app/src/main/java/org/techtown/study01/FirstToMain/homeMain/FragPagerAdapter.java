package org.techtown.study01.FirstToMain.homeMain;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag2;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag3;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag4;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag5;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag6;

import java.util.ArrayList;
import java.util.List;


public class FragPagerAdapter extends FragmentStateAdapter {
    // Real Fragment Total Count
    private final int mSetItemCount = 6; //화면에 출력될 프래그먼트 개수



    public FragPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) { //프래그먼트 순서에 맞게 넣어주세요.
        int iViewIdx = getRealPosition(position);
        switch( iViewIdx ) {
            case 0    : { return new Frag1(); }
            case 1    : { return new Frag2(); }
            case 2    : { return new Frag3(); }
            case 3    : { return new Frag4(); }
            case 4    : { return new Frag5(); }
            case 5    : { return new Frag6(); }
            default   : { return new Frag1(); } //기본으로 나와있는 프래그먼트
        }

    }

    public int getRealPosition(int _iPosition){
        return _iPosition % mSetItemCount;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() { //화면에 나오는 갯수 설정
        return 6; //여기서 무한 스크롤을 원하면 Integer.Max_Value를 해준다.
    }
}
