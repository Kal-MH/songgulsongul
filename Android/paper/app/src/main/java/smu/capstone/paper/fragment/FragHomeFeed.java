package smu.capstone.paper.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.LoginSharedPreference;
import smu.capstone.paper.R;
import smu.capstone.paper.adapter.HomeFeedAdapter;
import smu.capstone.paper.responseData.PostResponse;
import smu.capstone.paper.responseData.PostComu;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class FragHomeFeed extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    HomeFeedAdapter adapter;

    int user_id;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

    StatusCode statusCode;

    List<PostComu> feeds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_home_feed, container, false);
        //id 세팅
        user_id = LoginSharedPreference.getUserId(getActivity());



        recyclerView = rootView.findViewById(R.id.feed_recycler);

        //refresh
        swipeRefreshLayout = rootView.findViewById(R.id.feed_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);

                //데이터 변경
                GetFeedData();

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        GetFeedData();



        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setData(){
       if(feeds.size() == 0){
           recyclerView.setBackground( getActivity().getDrawable(R.drawable.no_post) );
       }
       adapter = new HomeFeedAdapter(getContext(), feeds);
       recyclerView.setAdapter(adapter);
    }

    // server 에서 data 전달
    public void GetFeedData(){
        serviceApi.GetFeed(user_id,20).enqueue(new Callback<PostResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse result = response.body();

                int resultCode = result.getCode();
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    feeds = result.getData();
                }
                else {
                    feeds=result.getData();
                }
                setData();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                feeds = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }
}
