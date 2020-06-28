package com.raghu.thetapracticle;


import android.app.Application;
import android.content.SharedPreferences;

import com.raghu.thetapracticle.extra.Constant;
import com.raghu.thetapracticle.repository.Repository;

public class AppMain extends Application {
    private static AppMain instance;
    private SharedPreferences mPreferences;
    public static Repository repositoryInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferences = getSharedPreferences(Constant.PREFERENCE_NAME, 0);
        instance = this;
    }

    public static AppMain getInstance() {
        return instance;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public static synchronized Repository getRepository() {
        if (repositoryInstance == null) {
            repositoryInstance = new Repository(instance);
        }
        return repositoryInstance;
    }
}
