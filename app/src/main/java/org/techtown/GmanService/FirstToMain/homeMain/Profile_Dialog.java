package org.techtown.GmanService.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.GmanService.FirstToMain.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Dialog extends Dialog {

    public static CircleImageView profileImage; //프로필 이미지
    public static Dialog dialog; //dialog 객체
    public static Button apply, cancelprofile, change_btn, defaultProfile;

    public static EditText changedName; //이름바꾸기 에딧텍스트

    public static String NN; //이름값이 바뀌었는지 아닌지 식별

    //프로필 변경하기

    public Profile_Dialog(@NonNull Context context) {
        super(context);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profile_card);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        params.width = (int) (dm.widthPixels * 0.8); //가로길이
        params.height = (int) (dm.heightPixels * 0.95); //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기

        profileImage = dialog.findViewById(R.id.circleImageView); //프로필 이미지
        changedName = dialog.findViewById(R.id.editchangeName); //이름 바꾸기 텍스트
        apply = dialog.findViewById(R.id.changeApply); //적용하기
        cancelprofile = dialog.findViewById(R.id.cancel_profile); //취소
        change_btn = dialog.findViewById(R.id.changeProfile); //프로필 이미지 변경 버튼
        defaultProfile = dialog.findViewById(R.id.defaultProfile); //기본 이미지 변경 버튼

        NN = HomeMain.nameView.getText().toString(); //원래이름 가져오기
        Profile_Dialog.changedName.setText(NN); //이름 입력란에 원래 이름 넣기(바뀌기 전 이름)
        downloadImg(HomeMain.num);
    }


    /**이미지 다운로드해서 가져오기 메서드 */
    private void downloadImg(int num) {
        //우선 디렉토리 파일 하나만든다.
        File file = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img"); //이미지를 저장할 수 있는 디렉토리
        //구분할 수 있게 /toolbar_images폴더에 넣어준다.
        //이 파일안에 저 디렉토리가 있는지 확인
        if (!file.isDirectory()) { //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        String filename = "profile" + num + ".jpg";
        storageRef.child("profile_img/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri2) {
                Log.d("오냐오냐", String.valueOf(uri2));
                Glide.with(getContext()).load(uri2).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

}
