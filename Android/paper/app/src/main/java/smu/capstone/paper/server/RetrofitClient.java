package smu.capstone.paper.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://ec2-3-34-42-7.ap-northeast-2.compute.amazonaws.com:3000/";
    private static Retrofit retrofit = null;

    private RetrofitClient(){

    }

    public static  Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 추가
                    .build();
        }

        return retrofit;
    }
}
