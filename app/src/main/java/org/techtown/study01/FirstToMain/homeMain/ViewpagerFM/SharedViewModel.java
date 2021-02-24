package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//라이브데이타 클래스 만들기

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> liveData = new MutableLiveData<>();

    public LiveData<String> getLiveData(){
        return liveData;
    }

    public void setLiveData(String str){
        liveData.setValue(str);
    }
}
