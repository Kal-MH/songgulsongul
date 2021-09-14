package com.smu.songgulsongul.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.smu.songgulsongul.LoginSharedPreference;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.recycler_adapter.HomeFeedAdapter;
import com.smu.songgulsongul.data.post.Response.PostFeedResponse;
import com.smu.songgulsongul.recycler_item.PostFeed;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragHomeFeed extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView background;
    HomeFeedAdapter adapter;

    int user_id;
    int lastId;

    Boolean isLoading = false;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    List<PostFeed> feeds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_home_feed, container, false);
        //id 세팅
        user_id = LoginSharedPreference.getUserId(getActivity());
        background = rootView.findViewById(R.id.feed_recycler_background);
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

        initScrollListener();
        GetFeedData();


        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setData() {
        if (feeds.size() == 0) {
            background.setImageDrawable(getActivity().getDrawable(R.drawable.no_post));
        }
        adapter = new HomeFeedAdapter(getContext(), feeds);
        recyclerView.setAdapter(adapter);
    }

    // server 에서 data 전달
    public void GetFeedData() {
        serviceApi.GetFeed(user_id, null).enqueue(new Callback<PostFeedResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {
                PostFeedResponse result = response.body();

                int resultCode = result.getCode();
                if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                    Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                } else if (resultCode == StatusCode.RESULT_OK) {
                    feeds = result.getData();
                } else {
                    feeds = result.getData();
                }
                setData();
            }

            @Override
            public void onFailure(Call<PostFeedResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                feeds = null;
                Log.d("feed", "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });
    }


    public void GetFeedDataMore() {
        lastId = feeds.get(feeds.size() - 1).getPost().getId();
        feeds.add(null);
        adapter.notifyItemInserted(feeds.size() - 1);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                serviceApi.GetFeed(user_id, lastId).enqueue(new Callback<PostFeedResponse>() {
                    @Override
                    public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {
                        feeds.remove(feeds.size() - 1);
                        adapter.notifyItemRemoved(feeds.size());

                        PostFeedResponse result = response.body();
                        int resultCode = result.getCode();
                        if (resultCode == StatusCode.RESULT_SERVER_ERR) {
                            Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                            // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                        } else if (resultCode == StatusCode.RESULT_OK) {
                            adapter.addItem(result.getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.addItem(result.getData());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<PostFeedResponse> call, Throwable t) {
                        Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                        feeds = null;
                        Log.d("feed", "통신 실패");
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                });
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d("scroll", layoutManager.findLastVisibleItemPosition() + " | " + (adapter.getItemCount() - 1));
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                        //리스트 마지막
                        GetFeedDataMore();
                        isLoading = true;
                        Log.d("home", "마지막!");
                    }
                }
            }
        });
    }
}
