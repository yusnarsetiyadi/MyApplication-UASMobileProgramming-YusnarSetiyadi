package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api;

import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ApiTodoModel;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ResponseApiTodoModel;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ResponseListApiTodoModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceNstack {
    @POST("todos")
    Call<ResponseApiTodoModel> createTodo(@Body ApiTodoModel apiTodoModel);
    @GET("todos")
    Call<ResponseListApiTodoModel> getAllTodos(@Query("page") int page, @Query("limit") int limit);
    @PUT("todos/{id}")
    Call<ResponseApiTodoModel> updateTodo(@Path("id") String id, @Body ApiTodoModel apiTodoModel);
    @DELETE("todos/{id}")
    Call<ResponseApiTodoModel> deleteTodo(@Path("id") String id);
}
