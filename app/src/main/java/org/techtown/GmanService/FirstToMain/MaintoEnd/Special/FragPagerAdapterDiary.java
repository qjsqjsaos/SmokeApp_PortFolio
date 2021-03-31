package org.techtown.GmanService.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class FragPagerAdapterDiary extends FragmentStateAdapter {
    // Real Fragment Total Count
    private final int mSetItemCount = 1; //화면에 출력될 프래그먼트 개수

    public FragPagerAdapterDiary(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) { //프래그먼트 순서에 맞게 넣어주세요.
        int iViewIdx = getRealPosition(position);
        switch( iViewIdx ) {
            default   : { return new DiaryFrag(); } //기본으로 나와있는 프래그먼트
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
        return 1; //여기서 무한 스크롤을 원하면 Integer.Max_Value를 해준다.
    }
}
