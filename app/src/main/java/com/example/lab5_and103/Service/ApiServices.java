package com.example.lab5_and103.Service;

import com.example.lab5_and103.Model.Distributor;
import com.example.lab5_and103.Model.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    public static final String BASE_URL = "http://192.168.137.1:3000/";
    @GET("/get-list-distributor")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @GET("/search-distributor")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);

    @POST("/add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);

    @PUT("/update-distributor/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id, @Body Distributor distributor);

    @DELETE("/delete-distributor/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);
}
