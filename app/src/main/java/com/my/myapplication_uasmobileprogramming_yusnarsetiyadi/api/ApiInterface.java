package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api;

import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/users")
    Call<List<UserModel>> getAllUsers();
}
