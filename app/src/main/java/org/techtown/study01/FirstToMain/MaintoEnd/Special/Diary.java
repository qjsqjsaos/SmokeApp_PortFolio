package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.FragPagerAdapter;

import java.util.ArrayList;

public class Diary extends Fragment {

    private ViewGroup viewGroup;
    private MaterialCalendarView materialCalendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.diary, container, false);

        materialCalendarView = viewGroup.findViewById(R.id.calendarView5);
        materialCalendarView.setSelectedDate(CalendarDay.today()); //오늘 날짜 큰 갈색 동그라미 표시

        setInit(); //뷰페이저 실행




        diaryWriteDate(2021, 3, 5);//일기 쓴 날짜 표시
        return viewGroup;

    }

    /** 일기 쓴 날짜 **/

    private void diaryWriteDate(int year, int month, int day) {
        int newMonth = month -1; //달은 -1 해줘야한다.

        //일기를 썼던 날짜리스트를 만든다.
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        calendarDayList.add(CalendarDay.from(year, newMonth, day)); //일기 쓴 날짜 표시

        EventDecorator eventDecorator = new EventDecorator(Color.GREEN, calendarDayList); //색표시 이벤트 데코레이터 호출
        materialCalendarView.addDecorators(eventDecorator);
    }


    /**
     * 뷰페이저 2 실행하기
     */
    private void setInit() {

        /* setup infinity scroll viewpager */
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.diaryViewpager); //여기서 뷰페이저를 참조한다.
        FragPagerAdapterDiary fragPagerAdapterDiary = new FragPagerAdapterDiary(getActivity());
        viewPageSetUp.setAdapter(fragPagerAdapterDiary);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(1); //좌우로 몇개까지 미리로딩하고 싶냐는 말이다. ex)5라고 입력시 1페이지에 있을때 나머지 2, 3, 4, 5, 6 페이지가 미리로딩된다는 뜻이다.
        viewPageSetUp.setSaveEnabled(false);

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * -(2 * pageOffset + pageMargin);
                if (-1 > position) {
                    page.setTranslationX(-offset);
                } else if (1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });

    }

}