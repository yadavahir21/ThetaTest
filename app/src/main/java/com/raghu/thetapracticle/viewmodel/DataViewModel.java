package com.raghu.thetapracticle.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.raghu.thetapracticle.AppMain;
import com.raghu.thetapracticle.database.DataTable;

public class DataViewModel extends AndroidViewModel {
    LiveData<DataTable> dataTableLiveData;

    public DataViewModel(@NonNull Application application) {
        super(application);
        dataTableLiveData = AppMain.getRepository().getDataTableLiveData();
    }

    public LiveData<DataTable> getDataTableLiveData(){
        return dataTableLiveData;
    }

}
