package com.brijir.averias.services;

import com.brijir.averias.bd.Fault;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FaultService {
    String KEY = "x-api-key: rabArf10E86thWRQ5u4MH3pFXVpiQiXv8jg1c4hO";

    @Headers(KEY)
    @GET("averias")
    Call<List<Fault>> getListFault();

    @Headers(KEY)
    @GET("averias/{id}")
    Call<Fault> getFault(@Path("id") String id);

    @Headers(KEY)
    @POST("averia/")
    Call<Fault> createFault(@Body Fault nuevo);

    @Headers(KEY)
    @POST("averia/{id}")
    Call<Fault> editFault(@Path("id") String id, @Body Fault edit);

    @Headers(KEY)
    @DELETE("averia/{id}")
    Call<ResponseBody> deleteFault(@Path("id") String id);
}
