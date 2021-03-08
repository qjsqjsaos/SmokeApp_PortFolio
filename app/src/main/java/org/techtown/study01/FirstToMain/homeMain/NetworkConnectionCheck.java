package org.techtown.study01.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.start.First_page_loading;

//네트워크 상황에 따른 콜백 메서드
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkConnectionCheck extends ConnectivityManager.NetworkCallback {   // 네트워크 변경에 대한 알림에 사용되는 Callback Class

    private Context context;
    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;
    public static Thread timeThread = null;

    public NetworkConnectionCheck(Context context){
        this.context=context;
        networkRequest =
                new NetworkRequest.Builder()                                        // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)       // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
                        .build();
        this.connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE); // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색
    }

    public void register() { this.connectivityManager.registerNetworkCallback(networkRequest, this);}

    public void unregister() {
        this.connectivityManager.unregisterNetworkCallback(this);
    }

    ////////////////////////////////////////다른거 다 해봤는데, 쓰레드로만 실행하자//////////////////////////////////////////////////////////////
    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        // 네트워크가 연결되었을 때 할 동작
    }


    @Override
    public void onUnavailable() {
        super.onUnavailable();
        //호출에 지정된 시간 초과 시간 내에 네트워크를 찾을 수 없을 때,
        Toast.makeText(context, "네트워크를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        timeThread = new Thread(new timeThread());
        timeThread.start(); //쓰레드실행
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        // 네트워크 연결이 끊겼을 때 할 동작
        timeThread = new Thread(new timeThread());
        timeThread.start(); //쓰레드실행

    }

    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String dateTime = bundle.getString("인터넷 미연결 액션");
            HomeMain.wiseView.setText(dateTime);
        }
    };




    public class timeThread implements Runnable {
        //타이머 쓰레드
        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("인터넷 미연결 액션", "오류");
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

}