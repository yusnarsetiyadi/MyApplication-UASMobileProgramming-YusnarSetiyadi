package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfigNstack {
    private static final String BASE_URL = "https://api.nstack.in/v1/";
    private static Retrofit retrofit = null;

    public static ApiInterfaceNstack getRetrofitClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterfaceNstack.class);
    }
}
