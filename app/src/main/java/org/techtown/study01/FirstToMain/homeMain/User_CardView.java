package org.techtown.study01.FirstToMain.homeMain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.study01.FirstToMain.R;

public class User_CardView extends LinearLayout {

    ImageView userView;
    TextView nameView;
    TextView dateView;
    TextView goalView;
    TextView moneyView;

    public User_CardView(Context context) {
        super(context);
        init(context);
    }

    public User_CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){ //카드뷰 인플레이션 진행
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user__card_view,this,true);

        userView = findViewById(R.id.userView);
        nameView = findViewById(R.id.nickName);
        dateView = findViewById(R.id.noSmoke_date);
        goalView = findViewById(R.id.noSmoke_goal);
        moneyView = findViewById(R.id.saveTheMoney);

    }

    public void setImageView (int i) {
        userView.setImageResource(i);
    }

    public void setNameView (String name) {
        nameView.setText(name);
    }

    public void setDateView (String date) {
        dateView.setText(date);
    }

    public void setGoalView (String goal) {
        goalView.setText(goal);
    }
    public void setMoneyView (String money) {
        moneyView.setText(money);
    }

}