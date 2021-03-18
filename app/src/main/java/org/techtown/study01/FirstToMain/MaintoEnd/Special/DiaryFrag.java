package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.io.File;

public class DiaryFrag extends Fragment implements CalendarView.OnDateChangeListener{

    private TextView diaryText;
    private ImageView diaryImage;
    private FloatingActionButton floatingActionButton;
    private static final int REQUEST_CODE = 0;
    private Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_frag, container, false );

        diaryText = view.findViewById(R.id.diaryText); //일기 텍스트
        diaryImage = view.findViewById(R.id.diaryImage); //일기 이미지
        floatingActionButton = view.findViewById(R.id.fab); //플로팅 버튼(일기 쓰기 버튼)

        WriteDiary(); //일기쓰기버튼

        return view;
    }

    /**일기 쓰기 메서드 (일기 쓰는 창(다이얼로그)을 열어준다.)*/
    private void WriteDiary() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diary_Dialog diary_dialog = new Diary_Dialog(getContext());

                //이미지 첨부 버튼 누를때
                diary_dialog.inputImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //인텐트를 통해 갤러리로 요청코드 보내기
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });

                //일기 취소 버튼 누를때
                diary_dialog.cancel_btn_diary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Diary_Dialog.dialog.dismiss(); //다이얼로그 창 닫기
                    }
                });

                //일기 작성완료 버튼 누를때

                diary_dialog.saveDiary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //제목과 내용 텍스트 가져오기
                        String title = Diary_Dialog.title_diary.getText().toString(); //일기 제목
                        String mainText = Diary_Dialog.mainText_diary.getText().toString(); //일기 본문

                    }
                });
            }
        });
    }

    /**달력 날짜를 선택할 때 여기서 날짜 값을 받아온다. */
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//      ex) textView.setText(FORMATTER.format(view.getDate()));

        int newMonth = month - 1; //달은 1일 빼줘야 정확해진다.

//        getFireBaseProfileDiary(HomeMain.num, year, newMonth, dayOfMonth); //식별넘버를 홈메인에서 가져온다. //그리고 날짜들을 넣어준다.

    }

    /**갤러리에서 이미지 받아오기 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData(); //사진 자료를 받는다.
    }

    //
//    // TODO: 2021-03-16 우선 파이어베이스 저장하는 법
//    /**파이어베이스로 프로필 이미지 저장 및 기존 이미지 삭제 */
//    private void createProfile_Photo_and_Delete() {
//        //storage
//        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
//        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
//        //파일명을 만들자.
//        String filename = "profile" + num + ".jpg";  //ex) profile1.jpg 로그인하는 사람에 따라 그에 식별값에 맞는 프로필 사진 가져오기
//        Uri file = uri;
//        Log.d("유알", String.valueOf(file));
//        //여기서 원하는 이름 넣어준다. (filename 넣어주기)
//        StorageReference riversRef = storageRef.child("profile_img/" + filename);
//        UploadTask uploadTask = riversRef.putFile(file);
//
//
//        // TODO: 2021-03-17 기존 일기 이미지와 일기 제목과 내용을 우선 삭제한다
//        // Create a reference to the file to delete
//        StorageReference desertRef = storageRef.child("profile_img/" + "profile" + num + ".jpg"); //삭제할 프로필이미지 명
//        // Delete the file
//        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        });
//
//        // TODO: 2021-03-17 새로운 프로필 이미지 저장
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getContext(), "프로필 이미지가 변경되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//    /** 일기 정보(제목, 배경, 내용, 날짜) 가져오기 */
//    private void getFireBaseProfileDiary(int num, int year, int month, int dayOfMonth) {
//        //우선 디렉토리 파일 하나만든다.
//        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/diary"+ num +"_album"); //이미지를 저장할 수 있는 디렉토리
//        //구분할 수 있게 /diary"+ num +"_album폴더에 넣어준다.
//        //이 파일안에 저 디렉토리가 있는지 확인
//        if (!file.isDirectory()) { //디렉토리가 없으면,
//            file.mkdir(); //디렉토리를 만든다.
//        }
//        downloadDiaryImage(num, year, month, dayOfMonth); //이미지 다운로드해서 가져오기 메서드
//    }
//
//    /**일기 이미지 다운로드해서 가져오기 메서드 */
//    private void downloadDiaryImage(int num, int year, int month, int dayOfMonth) {
//        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
//        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
//        storageRef.child("/diary"+ num +"_album" + "/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        });
//    }

}