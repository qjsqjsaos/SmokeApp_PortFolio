package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//라이브데이타 클래스 만들기

public class SharedViewModel extends ViewModel {
    //Int용 Frag2용 //금연할 일 수
    private MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public LiveData<Integer> getLiveData(){
        return liveData;
    }

    public void setLiveData(Integer integer){
        liveData.setValue(integer);
    }
    //Int용 Frag5용 //금연하면서 안 핀 담배 갯수
    private MutableLiveData<Integer> liveDataInt = new MutableLiveData<>();

    public LiveData<Integer> getLiveDataInt(){
        return liveDataInt;
    }

    public void setLiveDataInt(Integer integer){
        liveDataInt.setValue(integer);
    }

    //Long용 Frag3용 //금연하면서 안 핀 담배 갯수
    private MutableLiveData<Double> liveDataCost = new MutableLiveData<>();

    public LiveData<Double> getLiveDataCost(){
        return liveDataCost;
    }

    public void setLiveDataCost(Double double1){
        liveDataCost.setValue(double1);
    }


}
