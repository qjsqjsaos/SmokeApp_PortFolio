package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//라이브데이타 클래스 만들기

public class SharedViewModel extends ViewModel {
    //int용 Frag2용 //금연할 일 수
    private MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public LiveData<Integer> getLiveData(){
        return liveData;
    }

    public void setLiveData(Integer integer){
        liveData.setValue(integer);
    }


    //Long용 Frag5용 //금연하면서 안 핀 담배 갯수
    private MutableLiveData<Long> liveDataCount = new MutableLiveData<>();

    public LiveData<Long> getLiveDataCount(){
        return liveDataCount;
    }

    public void setLiveDataCount(Long longa){
        liveDataCount.setValue(longa);
    }



    //Long용 Frag3용 //금연하면서 안 핀 담배 갯수
    private MutableLiveData<Long> liveDataCost = new MutableLiveData<>();

    public LiveData<Long> getLiveDataCost(){
        return liveDataCost;
    }

    public void setLiveDataCost(Long longb){
        liveDataCost.setValue(longb);
    }


}
