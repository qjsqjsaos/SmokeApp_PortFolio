package org.techtown.study01.FirstToMain.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;

public class Register_Auth extends AppCompatActivity{

        private CheckBox checkBoxAll, check1, check2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.register_auth);

                checkBoxAll = (CheckBox) findViewById(R.id.all_check_btn);
                check1 = (CheckBox) findViewById(R.id.check_btn1);
                check2 = (CheckBox) findViewById(R.id.check_btn2);
                Button button = findViewById(R.id.button);

                checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                boolean checked = buttonView.isChecked();
                                        if (checked) {
                                                if (!check1.isChecked() && !check2.isChecked()) {
                                                        check1.setChecked(true);
                                                        check2.setChecked(true);
                                                }
                                        }
                                }
                        });

                button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                try {
                                        if (check1.isChecked() && check2.isChecked()) {
                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                startActivity(intent);
                                        } else if (check1.isChecked() && check2.isChecked() && checkBoxAll.isChecked()) {
                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                startActivity(intent);
                                        } else if(!check1.isChecked() && !check2.isChecked() && !checkBoxAll.isChecked()){
                                                Toast.makeText(getApplicationContext(), "항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                                                return;
                                        } else {
                                                Toast.makeText(getApplicationContext(), "필수 항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                                                return;
                                        }
                                }catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "시스템 오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                }

                        }
                });

        }
}