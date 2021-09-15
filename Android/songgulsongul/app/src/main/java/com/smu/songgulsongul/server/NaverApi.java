package com.smu.songgulsongul.server;

import com.smu.songgulsongul.data.shoppingapi.ShoppingResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverApi {
    @GET("v1/search/shop.json")
    Call<ShoppingResults> search(@Header("X-Naver-Client-Id") String clientId,
                                 @Header("X-Naver-Client-Secret") String clientPw,
                                 @Query("query") String query,
                                 @Query("display") Integer display,
                                 @Query("start") int start
    );

}
