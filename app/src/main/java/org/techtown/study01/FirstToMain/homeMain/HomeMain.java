package org.techtown.study01.FirstToMain.homeMain;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;
import androidx.room.Delete;
import androidx.viewpager2.widget.ViewPager2;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.HomeMain.HealthCheck;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1_Request;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag2;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag3;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag4;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag5;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag7;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag_ondestroy;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;
import org.techtown.study01.FirstToMain.register.NameRequest;
import org.techtown.study01.FirstToMain.register.Register;
import org.techtown.study01.FirstToMain.register.RegisterRequest;
import org.techtown.study01.FirstToMain.start.First_page_loading;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import static android.view.View.VISIBLE;


//홈 화면
public class HomeMain extends Fragment {

    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    //뷰그룹 부분
    private ViewGroup viewGroup;
    private ImageView userView;
    @SuppressLint("StaticFieldLeak")
    public static TextView nameView;
    private LinearLayout card;
    public static ImageView rank; //프로필 랭크 이미지

    public static TextView dateView; //프로필에 디데이날짜와 금연날짜이다. 금연시작버튼이나, 다이얼로그안에 금연취소버튼을 누를시 변동한다. 이 값은 Frag1으로 가서 초기화된다.

    //스태틱을 붙여서 Frag1에서 참조할 수 있게 한다. //금연하기 버튼과 취소버튼
    private Button noSmoke_Btn, stop_Btn;

    //NetworkConnectionCheck 참조할 수 있게 한다.
    public static TextView wiseView;

    public static String id;
    private String name;

    //쓰레드 부분
    private Thread timeThread = null;
    private final Boolean isRunning = true;

    private final String TAG = "MyTag"; //로그 찍을때,

    //저장 뷰모델
    private SharedViewModel sharedViewModel;


    //닉네임 중복체크
    private boolean nameCheck = false;

    public static int num; //프로필 이미지 식별값

    //로딩중 다이얼로그

    private Loading_Dialog loading_dialog;

    private AlertDialog dialog; //알림 다이아로그

    private Uri uri; //프로필 사진 자료

    /////////////////////Frag1이였던 것//////////////////////////

    //금연한지 얼마나 됬는지 날짜 값
    private long finallyDateTime;

    //Quest1에서 가져온 담배 핀 횟수와 비용 EditText
    /**
     * 이 카운트와 코스트는 다음에 값 전달하기
     */
    private long cigaCount;
    private long cigaCost; //이건 1초에 나타나는 비용이 소수점까지 가므로, long으로 표기한다.

    //중요 지정했던 시간이다. 디비에 넣었다가 뺄 때, 몇초 지났는지 구별해주는 시간이다.
    private String dateTime;

    //하루를 기준으로 피는 담배양을 하루 24시간으로 나눈 시간. ex) 하루에 10개비를 피면 2시간 24분 마다 핀것이다. 여기서 2시간 24분의 값을 초로 나타낸 것이다.
    private long last_cigaCount;
    private double last_cigaCost;

    public static Uri dialogwithUri;

    /**
     * 앱 종료시 쓰레드가 종료할 때만 쓰레드 종료(어차피 돌아오면 다시 켜짐)
     */
    @Override
    public void onPause() { //앱을 잠시 일시정시할 때 타이머 종료// 중첩 방지
        super.onPause();
        if (finallyDateTime > 0) { //쓰레드가 살아있을 때만 쓰레드를 종료하자
            timeThread.interrupt(); //쓰레드 종료
        }
    }


    /**
     * 앱이 맨 처음 실행될 때, 아이디값을 통해 정보를 가져온다.
     */
    @Override
    public void onResume() {
        super.onResume();
        loadingStart();//로딩창 보여주기
        idcheckandButton(); //아이디를 토대로 버튼정보가져오기

        }


    /**
     * 프로필 사진 갤러리에 요청시에 값을 여기서 받고 프로필 사진란에 이미지를 넣어준다.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                uri = data.getData();
                dialogwithUri = uri; //uri넣고, dialog로 보내기
                Log.d("유알", String.valueOf(uri));
                Glide.with(getContext()).load(uri).into(Profile_Dialog.profileImage); //다이얼로그 이미지사진에 넣기(일시적임)
                Glide.with(getContext()).load(uri).into(userView); //프로필 사진 이미지 넣기(일시적임)
                //파이어베이스에 내 새로운 프로필 이미지는 저장하고, 전에 이미지는 삭제한다.
                createProfile_Photo_and_Delete();

            } else if (resultCode == RESULT_CANCELED) {// 취소시 호출할 행동 쓰기
                Toast.makeText(getContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 2021-03-16 우선 파이어베이스 저장하는 법
    /**파이어베이스로 프로필 이미지 저장 및 기존 이미지 삭제 */
    private void createProfile_Photo_and_Delete() {
        createDir(); //디렉토리가 없으면 만들기
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        //파일명을 만들자.
        String filename = "profile" + num + ".jpg";  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기
        Uri file = uri;
        Log.d("유알", String.valueOf(file));
        //여기서 원하는 이름 넣어준다. (filename 넣어주기)
        StorageReference riversRef = storageRef.child("profile_img/" + filename);
        UploadTask uploadTask = riversRef.putFile(file);

        // TODO: 2021-03-17 기존 이미지 삭제
        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("profile_img/" + filename); //삭제할 프로필이미지 명
        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


        // TODO: 2021-03-17 새로운 프로필 이미지 저장
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "프로필 이미지가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // TODO: 2021-03-16 존나 중요!! 파이어베이스 이미지 (시작할때 프로필 가져오기)
    /**프로필 이미지 (파이어베이스 스토리지에서 가져오기) */
    private void getFireBaseProfileImage(int num) {
        createDir(); //디렉토리가 없으면 만들기
        downloadImg(num); //이미지 다운로드해서 가져오기 메서드
    }

    /**이미지 다운로드해서 가져오기 메서드 */
    private void downloadImg(int num) {
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        String filename = "profile" + num + ".jpg";
        storageRef.child("profile_img/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("오냐오냐", String.valueOf(uri));
                Glide.with(getContext()).load(uri).into(userView);
                dialogwithUri = uri; //첫 다이얼로그 프로필 보여주기
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    /**디렉토리 만들기(혹시 없을경우 대비해서) = 파이어베이스 */
    public void createDir(){
        //우선 디렉토리 파일 하나만든다.
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img"); //이미지를 저장할 수 있는 디렉토리
        //구분할 수 있게 /toolbar_images폴더에 넣어준다.
        //이 파일안에 저 디렉토리가 있는지 확인
        if (!file.isDirectory()) { //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        wiseView = viewGroup.findViewById(R.id.text_wisesay); //명언액자
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼
        stop_Btn = viewGroup.findViewById(R.id.ns_stop); //금연취소 버튼
        rank = viewGroup.findViewById(R.id.rank); //프로필 등급 이미지


        /**명언 랜덤 추출기 */
        int randomNumber = (int) (Math.random()*999); // 0~999중에서 랜덤 숫자 추출
        Log.d("랜덤넘버", String.valueOf(randomNumber));
        WiseSay_List wiseSay_list = new WiseSay_List();
        String WS = wiseSay_list.WiseArray(randomNumber); //여기에 랜덤숫자 만들어서 넣기
        wiseView.setText(WS);


        startNoSmokingButton(); //금연시작 버튼

        //텍스트뷰 글씨가 바뀔 때 호출한다.
        wiseView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //텍스트가 바뀌기전

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //텍스트가 바뀌는 중일 때,

            }

            @Override
            public void afterTextChanged(Editable s) { //텍스트가 바뀌고 난 후
                String wiseValue = wiseView.getText().toString();
                if (wiseValue.equals("오류")) {
                    popupLoading(); //firstloding창으로 이동하고,
                    BottomNavi.bottomNavi.finish(); //그 후에 뒤로가기 방지를 위해 아래있는 Bottomnavi를 없애준다.
                }
            }
        });



        setInit(); //뷰페이저 실행 메서드


        //BottomNavi에서 받은 번들 데이터
        Bundle bundle = this.getArguments();
        Log.d(TAG, "번들가져오고");

        name = bundle.getString("name");
        id = bundle.getString("id");
        Log.d(TAG, "번들 메세지들 다 가져옴");

        if (name != null) { //일반 로그인
            nameView.setText(name); //닉네임으로 이름바꿔주기
            Log.d(TAG, name);
        }


        /** 프로필을 클릭했을 때, 이름 변경 가능*/
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile_Dialog profile_dialog = new Profile_Dialog(getContext());
                profile_dialog.change_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //인텐트를 통해 갤러리로 요청코드 보내기
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });

                //적용하기 누를 때
                profile_dialog.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //만약 처음에 글씨가 그대로라면 적용하기를 누를 때 중복확인 상관없이 값 적용,
                        //하지만 닉네임이 처음 글씨와 다를 경우 중복체크 및 나머지 메서드 실행
                        String changeValue = Profile_Dialog.changedName.getText().toString();
                        loading_dialog.show(); //로딩창 띄우기
                        if (Profile_Dialog.NN.equals(changeValue)) {
                            Profile_Dialog.dialog.dismiss(); //다이어로그닫기
                            loading_dialog.dismiss(); //로딩창 닫기
                        }
                        if (!Profile_Dialog.NN.equals(changeValue)) {
                            String newName = Profile_Dialog.changedName.getText().toString(); //새로운 이름 가져오기
                            Log.d("궁금2", newName);
                            nickNameCheck(newName); //이름 중복체크
                        }
                    }
                });

                //취소 누를때
                profile_dialog.cancelprofile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Profile_Dialog.dialog.dismiss(); //다이얼로그닫기
                    }
                });

                //기본 이미지 설정 버튼 누를 때,
                profile_dialog.defaultProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable drawable = getResources().getDrawable(R.drawable.user); //기본이미지 드로어블로 가져오고
                        userView.setImageDrawable(drawable); //프로필사진에 기본이미지를 넣는다.
                        Profile_Dialog.profileImage.setImageDrawable(drawable); //다이얼로그에도 기본이미지를 넣는다.
                        dialogwithUri = null; //다이얼로그로 가는 uri에는 null값을 주어, 껏다켜도 기본이미지를 보이게 한다.

                        //그리고 기존에 있던 프로필 이미지를 삭제한다.
                        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
                        StorageReference storageRef = storage.getReference();//스토리지를 참조한다.
                        // Create a reference to the file to delete
                        StorageReference desertRef = storageRef.child("profile_img/" + "profile" + num + ".jpg"); //삭제할 프로필이미지 명
                        // Delete the file
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                    }
                });
            }
        });


        //금연 취소 눌렀을 때,
        stop_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //다이얼로그 띄우고,
                GiveUpNoSmoking_Dialog giveUpNoSmoking_dialog = new GiveUpNoSmoking_Dialog(getContext());
                giveUpNoSmoking_dialog.NSYES.setOnClickListener(new View.OnClickListener() { //다시도전하기 버튼을 누르면
                    @Override
                    public void onClick(View v) {
                        GiveUpNoSmoking_Dialog.dialog.dismiss(); //다이아로그 닫기
                    }
                });

                giveUpNoSmoking_dialog.NSNO.setOnClickListener(new View.OnClickListener() { //금연 포기 버튼을 누르면,
                    @Override
                    public void onClick(View v) {
                        loadingStart();//로딩창 보여주기
                        int value = 0;
                        String svalue = "0";
                        saveValueToDB(value, svalue); //디비에 값 0으로 초기화
                        noSmoke_Btn.setVisibility(VISIBLE); //금연하기 버튼 보이게 하고,
                        stop_Btn.setVisibility(GONE); //금연중지 버튼 없애기
                        timeThread.interrupt();//쓰레드 취소하기
                        GiveUpNoSmoking_Dialog.dialog.dismiss(); //다이아로그 닫기
                    }
                });

            }
        });

        return viewGroup;
    }






    /**
     * 프로필 바꾸기(적용버튼 사진만만) 메서드
     * @param image
     */

    private void changeProfileImageToDB(String image) {


        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Profile_Img_Check profile_img_check = new Profile_Img_Check(id, image, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(profile_img_check);

        Log.d("비트맵", String.valueOf(uri));
    }


    /**
     * 프로필 바꾸기(적용버튼 이름만) 메서드
     *
     * @param newName
     */

    private void applyProFile(String newName) {

        //우선 닉네임 형식에 맞는지
        if (!Pattern.matches("^[가-힣a-zA-Z0-9_]{2,10}$", newName)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            dialog = builder.setMessage("닉네임 형식을 지켜주세요.")
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
            loading_dialog.cancel(); //로딩창 닫기
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {  //새로운 이름 입력하기
                        nameView.setText(newName);
                        loading_dialog.cancel(); //로딩창 닫기
                        Profile_Dialog.dialog.dismiss(); //다이얼로그닫기
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ApplyProFile applyProFile = new ApplyProFile(id, newName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(applyProFile);

        Log.d("아디가뭐야", id);
        Log.d("아디가뭐야", newName);
    }

    /**
     * 금연하기 버튼을 클릭하고나서 금연시간 정하기
     */

    public void startNoSmokingButton() {

        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.
        noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
            @Override
            public void onClick(View v) {
                StartNoSmoking_Dialog dialog = new StartNoSmoking_Dialog(getContext());
                dialog.setDialogListener(new StartNoSmoking_Dialog.CustomDialogListener() {

                    @Override
                    public void onPositiveClicked(String date, String time) throws ParseException { //지정된 날짜, 지정된 시간
                        Calculate_Date calculate_date = new Calculate_Date();

                        /** 임시로 일단 값주기*/
                        cigaCount = 5;
                        cigaCost = 5000;

                        dateTime = date + " " + time; // 지정된 날짜와 데이트 시간 합치기
                        Log.d("3값", dateTime);

                        finallyDateTime = calculate_date.calTimeDateBetweenAandB(dateTime); //날 차이 구하기 (지정날짜와 시간만 넣기)


                        //문자를 지우는 함수다. dateTime에서 시간만 지우고 날짜만 출력한 값이다.
                        StringBuffer origin = new StringBuffer(dateTime);
                        StringBuffer justDate = origin.delete(10, 19);
                        Log.d("저스트", String.valueOf(justDate));
                        dateView.setText(justDate); //날짜 프로필에다가 넣어주기 //금연 시작 날짜

                        Log.d("3값", String.valueOf(finallyDateTime));

                        //하루 담배량 계산
                        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
                        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));
                        Log.d("시가카운트", String.valueOf(cigaCount));

                        //하루 담배값 계산
                        last_cigaCost = cigaCost / 86400; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
                        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));
                        Log.d("시가코스트", String.valueOf(cigaCost));

                        noSmoke_Btn.setVisibility(GONE); //금연버튼을 비활성화
                        stop_Btn.setVisibility(VISIBLE); //취소버튼을 활성화


                        saveValueToDB2(); //디비에 저장

                        timeThread = new Thread(new timeThread());
                        timeThread.start(); //쓰레드실행

                    }

                });
            }
        });
    }


    /**
     * 타이머 핸들러
     */
    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {

            //(msg.arg1 / 100) 이 1초이다. 1초는 1000단위이므로,
            //int min = (msg.arg1 / 100) / 60 같은 경우는 1/60이니까 분이다. (시간도 마찬가지)
            //쓰레드에서 번들정보 가져오기
            Bundle bundle = msg.getData();
            long dateTime = bundle.getLong("dateTime");
            Log.d("데이트타임", String.valueOf(dateTime));

            //타이머가 86400000 이 있으면 백의 자리에서 증감이 일어남 그래서,
            // dataTime에 0을 붙여서 천의 자리부터 숫자가 증가하게 만들어 올바른 타이머 동작을 구현했다.
            long datatime_last = Long.parseLong(dateTime + "0");
            Log.d("마지막데트", String.valueOf(datatime_last));
            /////////////////////////////////////
            long sec = (datatime_last / 1000) % 60; //초
            long min = (datatime_last / 1000) / 60 % 60; //분
            long hour = (datatime_last / 1000) / 3600 % 24; //시
            long day = datatime_last / (24 * 60 * 60 * 1000);//하루
            long ciga_Time = (datatime_last / 1000) / last_cigaCount; //담배를 피지 않은 횟수
            double ciga_Money = (datatime_last / 1000) * last_cigaCost; //지금껏 아낀 비용




            //스트링 열로 포맷한다.
            String result = String.format("%02d:%02d:%02d", hour, min, sec);

            Log.d("진짜", String.valueOf(msg.arg2));
            Log.d("리절트", result);
            Log.d("데이", String.valueOf(day));
            Log.d("타임", String.valueOf(ciga_Time));
            Log.d("머니", String.valueOf(ciga_Money));






            /** 실시간 데이터들 뷰모델로 각 프래그먼트로 전달*/
            sharedViewModel.setstartDate(result); //타이머 실시간 표시

            sharedViewModel.setLiveData(day); //ViewModel을 통해서 Frag2 Homemain으로 보내기 위해 Livedata에 oneDay를 보낸다.

            sharedViewModel.setLiveDataCount(ciga_Time); //ViewModel을 통해서 Frag5로 보내기 위해 Livedata에 ciga_Time를 보낸다.

            sharedViewModel.setLiveDataCost(ciga_Money); //ViewModel을 통해서 Frag3로 보내기 위해 Livedata에 ciga_Money를 보낸다.

            //setmax에 int형으로 밖에 못넣어서 1000으로 나눈다 => 0이 하나도 안붙어있는 숫자로만 전달
            long healthSecond = (datatime_last / 1000) * 1L;
            Log.d("헬스세컨드", String.valueOf(healthSecond));
            sharedViewModel.sethaelthSecond(healthSecond); //ViewModel을 통해서 HealthCheck과 Frag7로 보내기 위해 Livedata에 datatime을 보낸다.
        }
    };


    /**
     * 타이머 쓰레드
     */
    public class timeThread implements Runnable {
        //타이머 쓰레드
        @Override
        public void run() {
            long dateTime = finallyDateTime * 1L; //여기에는 날짜와 시간을 넣는데, 마찬가지로 초 형식으로 넣는다.
            Log.d("나", String.valueOf(dateTime));
            Log.d("나", String.valueOf(finallyDateTime));
            while (true) {
                while (isRunning) { //반복문으로 반복하기
                    //메세지에 번들정보 담아서 보내기
                    dateTime++;
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putLong("dateTime", dateTime);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(10); //혹시나 멈췄을 경우를 대비해 0.01초마다 쓰레드실행
                    } catch (InterruptedException e) { //인터럽트(취소 받을 경우) 한마디로 Bottomnavi에 있는 다이얼로그에서 금연 취소버튼을 눌렀을때이다.
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() { //취소 되고 나서, 실행 여기다가 적기
                                Frag1.textView.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag1.textView.setText("오늘을 기준으로\n\n00:00:00 시간 째 금연 중");
                                Frag2.textView2.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag2.textView2.setText("벌써 000일이 되었어요!");
                                Frag3.textView3.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag3.textView3.setText("0원");
                                Frag5.textView5.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag5.textView5.setText("0개비 가량 됩니다!");
                                dateView.setText("금연날짜");
                                Frag7.rankImg.setImageResource(0); //Frag7 이미지 없애기
                                Frag7.rankText.setText(""); //Frag7 빈칸만들기
                                rank.setImageResource(0); //프로필 이미지도 없애기

                                //HealthCheck이미지 값 초기화
                                if(HealthCheck.i1 != null){
                                    HealthCheck.i1.setImageResource(0);
                                    HealthCheck.i2.setImageResource(0);
                                    HealthCheck.i3.setImageResource(0);
                                    HealthCheck.i4.setImageResource(0);
                                    HealthCheck.i5.setImageResource(0);
                                    HealthCheck.i6.setImageResource(0);
                                    HealthCheck.i7.setImageResource(0);
                                    HealthCheck.i8.setImageResource(0);
                                    HealthCheck.i9.setImageResource(0);
                                    HealthCheck.i10.setImageResource(0);
                                    HealthCheck.i11.setImageResource(0);
                                    HealthCheck.i12.setImageResource(0);
                                    HealthCheck.i13.setImageResource(0);
                                    HealthCheck.i14.setImageResource(0);
                                    HealthCheck.i15.setImageResource(0);
                                    HealthCheck.i16.setImageResource(0);
                                    HealthCheck.i17.setImageResource(0);
                                    HealthCheck.i18.setImageResource(0);
                                    HealthCheck.i19.setImageResource(0);
                                }
                                //HealthCheck프로그레스 값 초기화
                                if(HealthCheck.pgb1 != null) {
                                    HealthCheck.pgb1.setProgress(0);
                                    HealthCheck.pgb2.setProgress(0);
                                    HealthCheck.pgb3.setProgress(0);
                                    HealthCheck.pgb4.setProgress(0);
                                    HealthCheck.pgb5.setProgress(0);
                                    HealthCheck.pgb6.setProgress(0);
                                    HealthCheck.pgb7.setProgress(0);
                                    HealthCheck.pgb8.setProgress(0);
                                    HealthCheck.pgb9.setProgress(0);
                                    HealthCheck.pgb10.setProgress(0);
                                    HealthCheck.pgb11.setProgress(0);
                                    HealthCheck.pgb12.setProgress(0);
                                    HealthCheck.pgb13.setProgress(0);
                                    HealthCheck.pgb14.setProgress(0);
                                    HealthCheck.pgb15.setProgress(0);
                                    HealthCheck.pgb16.setProgress(0);
                                    HealthCheck.pgb17.setProgress(0);
                                    HealthCheck.pgb18.setProgress(0);
                                    HealthCheck.pgb19.setProgress(0);
                                }
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소)
                    }
                }
            }
        }
    }


    /**
     * 뷰모델 저장소 다른 프래그먼트로 값을 전달한다.
     */
    //onCreateView에서 리턴해준 View(rootView)를 가지고 있다.
    //저장된 뷰가 반환된 직후에 호출됩니다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //뷰가 제공되는 경우 반환된 뷰를 가져옵니다.
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //지정된 Factory를 통해 ViewModel을 만들고 지정된 ViewModelStoreOwner의 저장소에 유지하는 ViewModelProvider를 만듭니다.

    }


    /**
     * 뷰페이저 2 실행하기
     */
    private void setInit() {

        /* setup infinity scroll viewpager */
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.SetupFrg_ViewPage_Info); //여기서 뷰페이저를 참조한다.
        FragPagerAdapter SetupPagerAdapter = new FragPagerAdapter(getActivity());
        viewPageSetUp.setAdapter(SetupPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(6); //좌우로 몇개까지 미리로딩하고 싶냐는 말이다. ex)5라고 입력시 1페이지에 있을때 나머지 2, 3, 4, 5, 6 페이지가 미리로딩된다는 뜻이다.
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


    /**
     * 퍼스트 페이지 로딩 페이지 띄우기 (인터넷 연결 안될시에)
     */

    public void popupLoading() { // 만약 인터넷이 연결이 되어 있지 않으면 인텐트를 한다.
        Intent intent = new Intent(getContext(), First_page_loading.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
        startActivity(intent);

    }


    /**
     * 금연을 포기하여 0으로 저장되는 곳
     */

    public void saveValueToDB(int value, String svalue) {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        loading_dialog.cancel(); //디비에 저장하면 로딩창 끄기

                        //디비에 저장했음.

                    } else {//실패
                        Toast.makeText(getContext(), "오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "디비오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //모두 취소된 값 0으로 저장
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(svalue, value, value, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);
        Log.d("뭐야", svalue + "/" + value + "/" + HomeMain.id);
    }


    /**
     * 버튼을 누른 값이 저장되는 곳 0이 아님
     */
    public void saveValueToDB2() {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        //디비에 저장했음.

                    } else {//실패
                        Toast.makeText(getContext(), "오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "디비오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //모두 취소된 값 0으로 저장
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(dateTime, cigaCount, cigaCost, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);
        Log.d("뭐야", dateTime + "/" + cigaCount + "/" + cigaCost + "/" + HomeMain.id);
    }


    /**
     * 로그인 하고나서 아이디를 통해 내 정보 불러오고 그의 맞게 버튼 호출
     */
    public void idcheckandButton() {
        if (id != null) {

            Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) { //우선 디비 접속이 성공하면, 정보들은 가져온다.

                            loading_dialog.cancel(); //접속성공하면 로딩창 끄기

                            //시간가져오기
                            dateTime = jsonObject.getString("datetime");
                            Log.d("디비정보", dateTime);

                            //이름가져오기
                            String newName = jsonObject.getString("name");
                            Log.d("디비정보", newName);

                            nameView.setText(newName); //이름 넣기

                            //이미지는 파이어베이스에서 가져온다.
                            //여기서는 식별값으로 num을 가져온다.
                            num = jsonObject.getInt("num");
                            Log.d("디비정보", String.valueOf(num));
                            getFireBaseProfileImage(num); //파이어베이스 프로필 사진 가져오기

                            //목표 가져오기
                            String goal = jsonObject.getString("goal");
                            Frag4.newGoalText.setText(goal);

                            if (dateTime.equals("0")) { //여기서 datetime이 0이면(아직 금연을 시작한게 아니거나, 이미 금연을 포기해서 값이 0인 경우)
                                //금연버튼 활성화
                                noSmoke_Btn.setVisibility(VISIBLE);
                                stop_Btn.setVisibility(GONE);
                                //쓰레드 실행안함.
                            } else {
                                //금연 취소버튼 활성화
                                noSmoke_Btn.setVisibility(GONE);
                                stop_Btn.setVisibility(VISIBLE);
                                startThreadShow(dateTime); //쓰레드 실행
                            }

                        } else {//실패
                            Toast.makeText(getContext(), "인터넷 접속이 원활하지 않습니다. 값이 저장되거나 불러오기 어렵습니다.", Toast.LENGTH_SHORT).show();
                            loading_dialog.cancel(); //접속성공하면 로딩창 끄기
                            return;
                        }


                    } catch (JSONException e) {
                        loading_dialog.cancel(); //접속성공하면 로딩창 끄기
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        loading_dialog.cancel(); //접속성공하면 로딩창 끄기
                        e.printStackTrace();
                    }
                }
            };

            Frag1_Request frag1_request = new Frag1_Request(id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(frag1_request);
        }
    }

    /**
     * 따로 만든 시작 쓰레드
     */
    private void startThreadShow(String dateTime) throws ParseException {
        Calculate_Date calculate_date = new Calculate_Date();

        finallyDateTime = calculate_date.calTimeDateBetweenAandB(dateTime); //날 차이 구하기 (지정날짜와 시간만 넣기)
        /** 임시로 일단 값주기*/
        cigaCount = 5;
        cigaCost = 5000;

        //하루 담배량 계산
        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));

        //하루 담배값 계산
        last_cigaCost = cigaCost / 86400; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));

        //스트링열 원하는 부분 제거
        StringBuffer origin = new StringBuffer(dateTime);
        StringBuffer justDate = origin.delete(10, 19);
        Log.d("저스트", String.valueOf(justDate));
        dateView.setText(justDate); //날짜 프로필에다가 넣어주기 //금연 시작 날짜

        timeThread = new Thread(new timeThread());
        timeThread.start(); //쓰레드실행
    }

    /**
     * 로딩창
     */
    public void loadingStart() {
        loading_dialog = new Loading_Dialog(getContext());
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }

    public void nickNameCheck(String dbname) {

        Response.Listener<String> responseListener = new Response.Listener<String>() { //결과 리스너 생성 (중복체크)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    if (success) {
                        nameCheck = true;
                        applyProFile(dbname); //프로필 이름 새로 저장
                    } else {
                        nameCheck = false;
                        dialog = builder.setMessage("존재하는 닉네임입니다.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        loading_dialog.cancel(); //로딩창 닫기
                        return;
                    }

                } catch (JSONException e) {
                    loading_dialog.cancel(); //로딩창 닫기
                    e.printStackTrace();
                    Toast.makeText(getContext(), "닉네임 오류, 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        NameRequest nameRequest = new NameRequest(dbname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(nameRequest);
    }

}

