package com.evan.ci_test.utils;

import com.evan.ci_test.Common.PhotoDetail;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET("/photos")
    Call<List<PhotoDetail>> getDetails();

    @GET
    Call<ResponseBody> getImage(@Url String url);

}
