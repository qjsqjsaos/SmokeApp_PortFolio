package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;

import android.view.View;

public interface OnDiaryItemClickListener {
    //클릭 리스너 구현을 위해 뷰홀더와 뷰와 position 넘버 보내기
    void onItemClick(RecyclerAdapter.ViewHolder holder, View view, int position);

}

