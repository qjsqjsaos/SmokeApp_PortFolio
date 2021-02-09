package org.techtown.study01.FirstToMain.findid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.register.GMailSender;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FindId extends AppCompatActivity {

    private static String IP_ADDRESS = "qjsqjsaos.dothome.co.kr";
    private static String TAG = "phptest";

    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        Button button_all = (Button) findViewById(R.id.sendId);
        button_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");
            }
        });

    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FindId.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

                Toast.makeText(getApplicationContext(),"오류입니다. 문의 부탁드립니다." , Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="qjsqjsaos";
        String TAG_ID = "id";
        String TAG_EMAIL = "email";
        String TAG_PW ="pw";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            TextView textView = findViewById(R.id.editTextEmail);

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                //데이터 베이스에서 가져온 자료들

                String dbId = item.getString(TAG_ID);
                Log.d(TAG, dbId); //아이디 값 잘 들어오는지 확인
                String dbEmail = item.getString(TAG_EMAIL);
                String dbPw = item.getString(TAG_PW);


                String emailText = textView.getText().toString();

            if(dbEmail != null){
                if (dbEmail.matches(emailText)) { // 입력한 이메일값과 가입한 적이 있는 이메일과 매치를 시켜 이메일로 보낸다. //가입한 적이 있으면 이메일 보내고 아니면 실패.

                    Log.d("oo", dbEmail); //1번
                    Log.d("oo", emailText);//2번 , 1번과 2번이 같아야함.


                    GMailSender gMailSender = new GMailSender("merrygoaround0726@gmail.com", "asdf4694");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    gMailSender.sendMail("금연투게더입니다. 아이디를 확인해주세요.", "금연투게더에서 인증번호는 : " + result + " 입니다. \n " +
                            "인증번호를 입력하시고 확인버튼을 누르시면 회원가입이 완료됩니다.", email.getText().toString());
                    Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                }else{

                }
            }
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}