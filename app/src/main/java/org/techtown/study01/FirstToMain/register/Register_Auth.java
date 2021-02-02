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
        private Boolean success = false;

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
                                if(checked){
                                        if(!check1.isChecked() && !check2.isChecked()) {
                                                check1.setChecked(true);
                                                check2.setChecked(true);
                                                success = true;
                                        }
                                        if(check1.isChecked() && !check2.isChecked()) {
                                                check2.setChecked(true);
                                        }
                                        if(!check1.isChecked() && check2.isChecked()) {
                                                check1.setChecked(true);
                                        }
                                        if(check1.isChecked() && check2.isChecked()) {
                                        }

                                        button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                        if(check1.isChecked() && check2.isChecked() || success == true){
                                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                                startActivity(intent);
                                                        }if(check1.isChecked() && check2.isChecked() && checkBoxAll.isChecked() || success == true){
                                                                Intent intent = new Intent(Register_Auth.this, Register.class);
                                                                startActivity(intent);
                                                        }else{
                                                                Toast.makeText(getApplicationContext(), "모두 선택해주세요.",Toast.LENGTH_SHORT).show();
                                                        }

                                                }
                                        });
                                }


                        }
                });

        }
}