package org.techtown.study01.FirstToMain.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class Register_Auth extends AppCompatActivity{

        private CheckBox checkBoxAll, check1, check2;
        private TextView serviceView, personalView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.register_auth);

                serviceView = findViewById(R.id.serviceView);
                personalView = findViewById(R.id.personalView);

                checkBoxAll = (CheckBox) findViewById(R.id.all_check_btn);
                check1 = (CheckBox) findViewById(R.id.check_btn1);
                check2 = (CheckBox) findViewById(R.id.check_btn2);
                Button button = findViewById(R.id.button);

                checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //전체 선택 눌렀을 시에 전부 선택.
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                boolean checked = buttonView.isChecked();
                                        if (checked) {
                                                if (!check1.isChecked() && !check2.isChecked()) { //전체선택이 되어 있고, 1번 2번이 선택 안되어있을때
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
                                        if (check1.isChecked() && check2.isChecked()) { //1번 2번 눌렀을 경우 참
                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                startActivity(intent);
                                        } else if (check1.isChecked() && check2.isChecked() && checkBoxAll.isChecked()) { //전체선택 1번 2번 눌렀을 경우 참
                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                startActivity(intent);
                                        } else if(!check1.isChecked() && !check2.isChecked() && !checkBoxAll.isChecked()){ //다 클릭 안했을 때 참
                                                Toast.makeText(getApplicationContext(), "항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                                                return;
                                        } else { //그 외 나머지.
                                                Toast.makeText(getApplicationContext(), "필수 항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                                                return;
                                        }
                                }catch (Exception e) { //오류 발생할 시 대비
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "시스템 오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                }

                        }
                });

                serviceView.setOnClickListener(new View.OnClickListener() { //이용약관 동의 약관보기
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(Register_Auth.this, Terms_Service.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                        }
                });

                personalView.setOnClickListener(new View.OnClickListener() { //개인정보 처리방침 동의 보기
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(Register_Auth.this, Terms_Personal.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                        }
                });


        }
}