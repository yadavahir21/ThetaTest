package com.raghu.thetapracticle.retrofit;


import com.raghu.thetapracticle.model.MainResponce;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestApi {

    @GET("users")
    Call<MainResponce> getMainResponce(@Query("page") int page);
}
