package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.study01.FirstToMain.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    ArrayList<DiaryInfo_GetterSetter> items = new ArrayList<DiaryInfo_GetterSetter>();
    //DiaryInfo_GetterSetter에 들어있는 아이템들을 넣고, int로 꺼내주기 용이하게 ArrayList를 하나 만든다.

    //뷰홀더가 만들어질 때 호출
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //가각의 뷰타입에 따라 xml레이아웃을 인플레이션해서 보여줘야한다.
        //인플레이션을 위해선 인자로 넘어온 ViewGroup에 Context객체가 필요하다.)
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        //인플레이션을 통해 뷰 객체를 만든다.
        View itemView = inflater.inflate(R.layout.recycler_itemview, viewGroup, false);

        return new ViewHolder(itemView); //이 코드 제일 아래있는 ViewHolder 클래스에 있는 ViewHolder생성자에 아이템 뷰를 전달하고,
                                        //새로운 뷰홀더 객체를 만들어 반환한다.
    }

    //뷰홀더가 재사용될 때 호출(보통은 아이템에 개수만큼 객체가 만들어지지 않는다.
    // 예를 들어 뷰가 천개면 천개의 객체가 만들어지지 않고, 처음 뷰가 안보일 때 아래쪽에서 재사용하게 만들어줘야 한다.)
    // 그래서 이 메서드에서 재사용을 해줘야한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DiaryInfo_GetterSetter item = items.get(position); //재사용 할때 넘어오는 포지션(번호)에 따라 데이터를 가져와서 DiaryInfo_GetterSetter객체에 넣어주고,
        viewHolder.setItem(item); //코드 아래쪽 ViewHolder클래스에 setItem에다가 넣어주어, 값을 전달받은 뷰 객체를 재사용한다.
        //한마디로 ArrayList에서 번호대로 꺼내서 재사용하는 것
    }

    //어뎁터에서 관리하는 아이템의 개수를 반환, 관리하는 갯수가 몇개인지 여기서 설정
    @Override
    public int getItemCount() {
        return items.size(); //ArrayList안에 값이 얼마나 있는지 계산하고, 그 값만큼 반환한다.(보여준다)
    }

    /*DiaryInfo_GetterSetter 객체를 ArrayList안에 넣어서 관리하기 때문에 여기 어댑터에서 DiaryInfo_GetterSetter객체를 넣거나 가져 갈 수 있도록
    * 다음과 같은 addItem(), setItems(), getItem(), setItem() 메서드를 추가한다. */

    public void addItem(DiaryInfo_GetterSetter item){ //DiaryInfo_GetterSetter객체를 ArrayList에 add한다.
        items.add(item);
    }

    public void setItems (ArrayList<DiaryInfo_GetterSetter> items){ //ArrayList에 ArrayList<DiaryInfo_GetterSetter>객체를 넣는다.
        this.items = items;
    }

    public DiaryInfo_GetterSetter getItem (int position){ //ArrayList에 입력값을 넣어 값에 맞는 DiaryInfo_GetterSetter객체를 반환한다.
        return items.get(position);
    }

    public void setItem (int position, DiaryInfo_GetterSetter item){ //ArrayList에 postion값과 DiaryInfo_GetterSetter 객체를 set한다.
        items.set(position, item);
    }


    //뷰홀더 (각 아이템을 보여줄 뷰를 이 뷰홀더에 담아두게 된다.)
    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView recyclerTitle; //금연 제목
        private TextView recyclerDays; //금연 몇일차
        private TextView recyclerWriteDate; //금연 일기 쓴 날짜

        //생성자에서 참조
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //뷰홀더의 생성자에 전달되는 뷰 객체에 들어있는 텍스트 뷰를 참조한다.
            recyclerTitle = itemView.findViewById(R.id.recyclerTitle);
            recyclerDays = itemView.findViewById(R.id.recyclerDays);
            recyclerWriteDate = itemView.findViewById(R.id.recyclerWriteDate);
        }


        public void setItem (DiaryInfo_GetterSetter diaryInfo_getterSetter){
            recyclerTitle.setText(diaryInfo_getterSetter.getR_title());
            recyclerDays.setText(diaryInfo_getterSetter.getR_days());
            recyclerWriteDate.setText(diaryInfo_getterSetter.getR_writeDate());
        }
    }
}
