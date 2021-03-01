package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//라이브데이타 클래스 만들기

public class SharedViewModel extends ViewModel {
    //Long용 Frag2용 //금연할 일 수
    private MutableLiveData<Long> liveData = new MutableLiveData<>();

    public LiveData<Long> getLiveData(){
        return liveData;
    }

    public void setLiveData(Long longa){
        liveData.setValue(longa);
    }


    //Long용 Frag5용 //금연하면서 안 핀 담배 갯수
    private MutableLiveData<Long> liveDataCount = new MutableLiveData<>();

    public LiveData<Long> getLiveDataCount(){
        return liveDataCount;
    }

    public void setLiveDataCount(Long longa){
        liveDataCount.setValue(longa);
    }



    //Long용 Frag3용 //금연하면서 생긴돈
    private MutableLiveData<Double> liveDataCost = new MutableLiveData<>();

    public LiveData<Double> getLiveDataCost(){
        return liveDataCost;
    }

    public void setLiveDataCost(Double longa){
        liveDataCost.setValue(longa);
    }


}
