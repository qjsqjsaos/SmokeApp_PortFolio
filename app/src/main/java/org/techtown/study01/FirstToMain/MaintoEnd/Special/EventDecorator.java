package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 이벤트 데코더 이 곳에서 메터리얼 캘린더뷰 라이브러리의 성능을 사용가능
 */
public class EventDecorator implements DayViewDecorator {

  private int color;
  private HashSet<CalendarDay> dates;

  public EventDecorator(int color, ArrayList<CalendarDay> dates) {
    this.color = color;
    this.dates = new HashSet<>(dates);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return dates.contains(day);
  }

  @Override
  public void decorate(DayViewFacade view) {
    view.addSpan(new DotSpan(5, color));
  }
}
