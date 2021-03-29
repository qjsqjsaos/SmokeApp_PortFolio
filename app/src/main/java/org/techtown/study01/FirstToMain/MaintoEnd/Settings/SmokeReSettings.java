package org.techtown.study01.FirstToMain.MaintoEnd.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.getDiaryInfo_Request;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;

public class SmokeReSettings extends AppCompatActivity {

    private TextInputEditText cigaCount, cigaCost;
    private Button resettingSmoke, cancelsettings_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smoke_re_settings);

        setInit();

        clickListener();
    }
    /**리스너 모음 */
    private void clickListener() {
        resettingSmoke.setOnClickListener(v -> {
            updateSmokeSettings();
        });
    }



    /**참조하기 */
    private void setInit() {
        cigaCount = findViewById(R.id.cigaCount);
        cigaCost = findViewById(R.id.cigaCost);
        resettingSmoke = findViewById(R.id.resettingSmoke);
        cancelsettings_btn = findViewById(R.id.cancelsettings_btn);
    }
    /**흡연량과 흡연비용 업데이트 하기 */
    private void updateSmokeSettings() {

       int newCigaCost = Integer.parseInt(cigaCost.getText().toString());
       int newCigaCount = Integer.parseInt(cigaCount.getText().toString());

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        HomeMain homeMain = new HomeMain();
                        Bundle bundle = new Bundle();
                        bundle.putLong("cigaCost", newCigaCost);
                        bundle.putLong("cigaCount", newCigaCount);
                        homeMain.setArguments(bundle);

                    } else {//실패
                        Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Smoke_Settings_Request smoke_settings_request = new Smoke_Settings_Request(newCigaCount, newCigaCost, HomeMain.num, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(smoke_settings_request);

    }



}