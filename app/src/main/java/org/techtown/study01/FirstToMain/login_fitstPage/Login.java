//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findPW.FindPw;
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.register.Register;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.text.TextUtils.isEmpty;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
        private static final String TAG = "Login"; //로그 찍을때,
        public static Context mcontext; //context 변수 선언
        public Intent intent; //구글 로그인 인텐트
        public Intent intent2; //카카오 로그인 인텐트


        private EditText idText, passwordText; //아이디 비밀번호 입력
        private Button btn_login;//로그인버튼
        private TextView rg_sign, btn_findId, btn_findPw, btn_google; ; //회원가입버튼, 아이디 비번찾기 버튼

        //카카오 로그인
        private View kakao_login_btn; //카카오 로그인 버튼





        //파이어베이스 구글 로그인
        private GoogleSignInButton signInButton; //구글 로그인 버튼
        private FirebaseAuth auth; //파이어 베이스 인증 객체
        private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
        private static  final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_fistpage);

            mcontext = this; // onCreate에서 this 할당

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



            /** 카카오 로그인 연동 코드*/

            kakao_login_btn = findViewById(R.id.kakaoLogin);

            //콜백 객체 만들어서 callback 변수에 넣고 아래 setOnclickListener에서 사용
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    //토큰이 전달 되면 로그인 성공, 아니면 null값 받고 실패
                    Log.d("토큰", String.valueOf(oAuthToken));
                    if(oAuthToken != null) {

                        //로그인이 되었을 때,

                        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                            @Override
                            public Unit invoke(User user, Throwable throwable) { //로그인이 되어있는지 확인하는 메서드(카카오)
                                if(user != null) {
                                    Log.d("유저", String.valueOf(user));

                                    intent2 = new Intent(getApplicationContext(), BottomNavi.class);
                                    intent2.putExtra("nickName_kakao", user.getKakaoAccount().getProfile().getNickname());
                                    intent2.putExtra("photoUrl_kakao", user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                                    startActivity(intent2);

                                    Log.i(TAG, "invoke : id = " + user.getId());
                                    Log.i(TAG, "invoke : email = " + user.getKakaoAccount().getEmail());
                                    Log.i(TAG, "invoke : nickName = " + user.getKakaoAccount().getProfile().getNickname());
                                    Log.i(TAG, "invoke : gender = " + user.getKakaoAccount().getGender());
                                    Log.i(TAG, "invoke : age = " + user.getKakaoAccount().getAgeRange());

                                    // 프로필 사진 => 변수_수열 = user.getKakaoAccount().getProfile().getThumbnailImageUrl();
                                    // 넣을때는 Glide.with(넣을 이미지 뷰).load(변수_수열).circleCrop().into(넣을 이미지 뷰);

                                }
                                if (throwable != null) {
                                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());

                                }
                                return null;
                            }
                        });
                    }
                    if(throwable != null) {
                        //로그인이 되었을 때,
                    }
                    return null;
                }
            };

            kakao_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //설치하는 디바이스에 카카오톡이 설치 유무를 알아낸다.
                    if(LoginClient.getInstance().isKakaoTalkLoginAvailable(Login.this)) { //설치되어 있을 때,

                        LoginClient.getInstance().loginWithKakaoTalk(Login.this, callback);


                    } else { //디비이스에 설치가 되어 있지 않을 때

                        LoginClient.getInstance().loginWithKakaoAccount(Login.this, callback);
                    }
                }
            });





        /** 파이어 베이스 구글 아이디 연동 코드*/

            //기본적인 구글 옵션 세팅
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            //액티비티에서 사용하면 객체는 this, 프래그먼트 사용시 getContext사용하기
            googleApiClient = new GoogleApiClient.Builder(this)
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

                            intent = new Intent(getApplicationContext(), BottomNavi.class);
                            intent.putExtra("nickName", account.getDisplayName());
                            intent.putExtra("photoUrl", String.valueOf(account.getPhotoUrl()));
                            startActivity(intent);


                            //인텐트를 통해 Login액티비티에서 BottomNavi액티비티로 도착한다.
                            //그리고 BottomNavi액티비티에서 HomeMain프래그먼트로 번들에 담아 데이터를 보낸다.
                            //HomeMain에서 데이터를 꺼내 사용(중요)

                        }else {//로그인이 실패했으면..
                            Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /** 카카오 updateKakaoLoginUi 메서드*/

    public void updateKakaoLoginUi(){ //여기서 로그인 정보를 호출해서 확인하고, 정보를 보낼 수 있다.

    }

}