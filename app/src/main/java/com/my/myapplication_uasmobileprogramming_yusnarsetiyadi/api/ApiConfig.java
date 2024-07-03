package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static Retrofit retrofit = null;

    public static ApiInterface getRetrofitClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
