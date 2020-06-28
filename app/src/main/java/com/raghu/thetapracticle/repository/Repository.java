package com.raghu.thetapracticle.repository;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.raghu.thetapracticle.database.DataDao;
import com.raghu.thetapracticle.database.DataTable;
import com.raghu.thetapracticle.database.Database;
import com.raghu.thetapracticle.database.UserDao;
import com.raghu.thetapracticle.database.UserTable;
import com.raghu.thetapracticle.model.Data;
import com.raghu.thetapracticle.model.MainResponce;
import com.raghu.thetapracticle.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Repository {
    private static final String TAG = Repository.class.getSimpleName();
    public static int TOTAL_PAGES = 999999999;
    private UserDao userDao;
    private DataDao dataDao;
    private LiveData<DataTable> dataTableLiveData = new MutableLiveData<>();

    public Repository(Application application) {
        userDao = Database.getDatabase(application).userDao();
        dataDao = Database.getDatabase(application).dataDao();
        dataTableLiveData = dataDao.getDataList();
    }

    public long insertUser(UserTable userTable) {
        return userDao.insertUser(userTable);
    }

    public UserTable loginValidation(String emailID, String password) {
        return userDao.loginValidation(emailID, password);
    }

    public UserTable findEmail(String emailID) {
        return userDao.findEmail(emailID);
    }

    public void updateUser(String username, String password, String emailID, String photoPath, int userID) {
        userDao.updateUser(username, password, emailID, photoPath, userID);
    }

    public void deleteAll() {
        userDao.deleteAllUser();
    }

    public LiveData<DataTable> getDataTableLiveData() {
        return dataTableLiveData;
    }

    public void loadApi(final boolean isloadMore, int page) {
        Call<MainResponce> call = RetrofitClient.getInstance().getApi().getMainResponce(page);
        call.enqueue(new Callback<MainResponce>() {
            @Override
            public void onResponse(Call<MainResponce> call, Response<MainResponce> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                Log.d(TAG, "onResponse: body: " + response.body());
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Log.d(TAG, "onResponse: getTotalPages: " + response.body().getTotalPages());
                    TOTAL_PAGES = response.body().getTotalPages();
                    Log.d(TAG, "onResponse: totalPage: " + TOTAL_PAGES);
                    List<Data> dataList = new ArrayList<>();
                    DataTable dataTable = dataDao.findDataTable();
                    if (dataTable != null) {
                        if (isloadMore) {
                            dataList.addAll(dataTable.dataList);
                            dataList.addAll(response.body().getData());
                        } else {
                            dataList.addAll(response.body().getData());
                        }
                        dataDao.updateData(dataList, dataTable.dataId);
                    } else {
                        DataTable newDataTable = new DataTable();
                        newDataTable.dataList = response.body().getData();
                        dataDao.insertData(newDataTable);
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponce> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

}
