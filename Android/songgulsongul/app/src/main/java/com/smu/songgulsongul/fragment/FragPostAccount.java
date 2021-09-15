package com.smu.songgulsongul.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.recycler_adapter.UserlistAdapter;
import com.smu.songgulsongul.data.user.response.SearchIdResponse;
import com.smu.songgulsongul.recycler_item.User;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragPostAccount extends Fragment {

    private View view;
    UserlistAdapter adapter;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    List<User> accountData;

    RecyclerView recyclerView;
    String keyword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = (ViewGroup) inflater.inflate(R.layout.frag_post_account, container, false);
        recyclerView = view.findViewById(R.id.frag_account_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        //Activity에서 가져옴
        keyword = this.getArguments().getString("keyword");
        getAccountData();

        return view;
    }




    // server에서 data전달
    public void getAccountData(){
        Log.d("search" , "FragPostTag--> " + keyword);

        serviceApi.SearchPostId(keyword, 0 ).enqueue(new Callback<SearchIdResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<SearchIdResponse> call, Response<SearchIdResponse> response) {
                SearchIdResponse result = response.body();

                int resultCode = result.getCode();
                if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == StatusCode.RESULT_OK){
                    accountData = result.getData();
                }
                else {
                    accountData = result.getData();
                }
                setAccountData();
            }

            @Override
            public void onFailure(Call<SearchIdResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                accountData = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }
    // server에서 data전달
    public void getAccountData(String query){
        Log.d("search" , "FragPostTag--> " + query);

        serviceApi.SearchPostId(query, 0 ).enqueue(new Callback<SearchIdResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<SearchIdResponse> call, Response<SearchIdResponse> response) {
                SearchIdResponse result = response.body();

                int resultCode = result.getCode();
                if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == StatusCode.RESULT_OK){
                    accountData = result.getData();
                }
                else {
                    accountData = result.getData();
                }
                setAccountData();
            }

            @Override
            public void onFailure(Call<SearchIdResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                accountData = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAccountData(){
        if(accountData.size() == 0)
            recyclerView.setBackground( getActivity().getDrawable(R.drawable.no_post) );

        else
            recyclerView.setBackgroundColor(Color.argb(0,10,10,10));

        adapter = new UserlistAdapter(this.getContext(),accountData);
        recyclerView.setAdapter(adapter);

    }

}
