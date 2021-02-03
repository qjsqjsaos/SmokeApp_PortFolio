//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findPW.FindPw;
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.register.Register;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.text.TextUtils.isEmpty;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


        private EditText idText, passwordText; //아이디 비밀번호 입력
        private Button btn_login;//로그인버튼
        private TextView rg_sign, btn_findId, btn_findPw, btn_google; ; //회원가입버튼

        //파이어베이스 구글 로그인
        private SignInButton signInButton; //구글 로그인 버튼
        private FirebaseAuth auth; //파이어 베이스 인증 객체
        private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
        private static  final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_fistpage);

            idText = findViewById(R.id.idText);
            passwordText = findViewById(R.id.passwordText);

            rg_sign = findViewById(R.id.rg_sign);
            rg_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Register.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            btn_login = findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = idText.getText().toString();
                    String pw = passwordText.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                               JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {//로그인 성공시
                                    String id = jsonObject.getString("id");
                                    String pw = jsonObject.getString("pw");
                                    String name = jsonObject.getString("name");


                                    Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, BottomNavi.class);

                                    intent.putExtra("id", id);
                                    intent.putExtra("pw", pw);

                                    startActivity(intent);

                                } else {//로그인 실패시
                                    Toast.makeText(getApplicationContext(), "아이디/비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(id, pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);

                }
            });

            btn_findId = (TextView) findViewById(R.id.tv_id);
            btn_findPw = (TextView) findViewById(R.id.tv_pass);

            //아이디 찾기 화면으로 이동
            btn_findId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, FindId.class);
                    startActivity(intent);
                }
            });

            //비밀 번호 찾기 화면으로 이동
            btn_findPw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(Login.this, FindPw.class);
                    startActivity(intent2);
                }
            });

        /** 여기서 부터 파이어 베이스 구글 아이디 연동 코드*/

            //기본적인 구글 옵션 세팅
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            //액티비티에서 사용하면 객체는 this, 프래그먼트 사용시 getContext사용하기
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();

            auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체 초기화
            signInButton = findViewById(R.id.btn_google);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, REQ_SIGN_GOOGLE);
                    //보내고 다시 받을거임

                }
            });


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //다시 가져오는 메서드 // 구글 로그인 인증을 요청했을 때, 결과 값을 되돌려 받는 메서드

        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){ //인증 결과가 성공적이면
                GoogleSignInAccount account = result.getSignInAccount(); //데이터가 여기에 다 담겨있음, 구글 로그인 정보를 담고있다.
                //닉네임 프로필사진 이메일 등등


                resultLogin(account); //로그인 결과 값 출력하라는 메서드

            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {//task는 인증결과과
                        if(task.isSuccessful()) { //로그인이 성공했으면,
                            Toast.makeText(Login.this, "로그인 성공", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(Login.this, BottomNavi.class);
                            //액티비티에서 프래그먼트로는 Bundle을 사용하고, 액티비티간에는 Intent를 사용한다.
                            HomeMain homeMain = new HomeMain();
                            Bundle bundle = new Bundle(); //어카운트 데이터 써먹기(중요)
                            bundle.putString("nickName", account.getDisplayName());
                            bundle.putString("photoUrl", String.valueOf(account.getPhotoUrl()));
                            //valueOf는 특정 자료형을 스트링형태로 변환 할 때 사용.
                            homeMain.setArguments(bundle);

                            startActivity(intent);

                        }else {//로그인이 실패했으면..
                            Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}