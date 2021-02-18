package org.techtown.study01.FirstToMain.homeMain;

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


public class FragPagerAdapter extends FragmentStateAdapter {
    // Real Fragment Total Count
    private final int mSetItemCount = 6;

    public FragPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int iViewIdx = getRealPosition(position);
        switch( iViewIdx ) {
            case 0    : { return new Frag1(); }
            case 1    : { return new Frag2(); }
            case 2    : { return new Frag3(); }
            case 3    : { return new Frag4(); }
            case 4    : { return new Frag5(); }
            case 5    : { return new Frag6(); }
            default   : { return new Frag1(); }
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
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
