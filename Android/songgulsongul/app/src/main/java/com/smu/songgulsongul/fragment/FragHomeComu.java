package com.smu.songgulsongul.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.activity.PostSearchActivity;
import com.smu.songgulsongul.recycler_adapter.PostImageRVAdapter;
import com.smu.songgulsongul.recycler_item.Post;
import com.smu.songgulsongul.data.post.Response.PostListResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragHomeComu extends Fragment {
    private View view;
    private SearchView searchView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    PostImageRVAdapter adapter;
    List<Post> postData;

    int lastId;
    boolean isLoading = false;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    LinearLayout loadlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_comu, container, false);

        // id 세팅
        recyclerView = view.findViewById(R.id.comu_rv);
        searchView = view.findViewById(R.id.comu_search);
        swipeRefreshLayout = view.findViewById(R.id.comu_refresh_layout);
        loadlayout = view.findViewById(R.id.load_layout);
        loadlayout.setVisibility(View.GONE);
        loadlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리스트 마지막
                GetCommunityDataMore();
                isLoading = true;
                Log.d("home" ,"마지막!");
                loadlayout.setVisibility(View.GONE);
            }
        });


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // 검색 버튼 눌렀을 시 발생
                Intent intent = new Intent(getActivity(), PostSearchActivity.class);
                intent.putExtra("keyword",query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 검색어 입력 시 발생
                return false;
            }
        });


        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchView.findViewById(id);
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_light);
        searchText.setTypeface(tf);



        GetCommunityData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                //데이터 변경
                GetCommunityData();
            }
        });

        initScroll();
        GetCommunityData();

        return view;
    }

    //server에서 data전달
    public void GetCommunityData(){
        serviceApi.GetCommunity(null).enqueue((new Callback<PostListResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                PostListResponse result = response.body();

                int resultCode = result.getCode();
                if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == StatusCode.RESULT_OK){
                    postData = result.getData();
                }
                else {
                    postData = result.getData();
                }

                setData();
                initScroll();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
              //  feeds = new JsonObject();
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        }));
    }

    public void GetCommunityDataMore(){

        lastId = postData.get(postData.size()-1).getId();
        postData.add(null);
        adapter.notifyItemInserted(postData.size() - 1);

        loadlayout.setVisibility(View.GONE);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                serviceApi.GetCommunity(lastId).enqueue((new Callback<PostListResponse>() {
                    @Override
                    public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                        postData.remove(postData.size()-1);
                        adapter.notifyItemRemoved(postData.size());

                        final PostListResponse result = response.body();
                        int resultCode = result.getCode();
                        if(resultCode == StatusCode.RESULT_SERVER_ERR){
                            Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                            // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                        }
                        else if( resultCode == StatusCode.RESULT_OK){

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //업데이트
                                    adapter.addItem(result.getData());
                                    adapter.notifyDataSetChanged();

                                }
                            });
                        }
                        else {
                            adapter.addItem(result.getData());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<PostListResponse> call, Throwable t) {
                        Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                        //  feeds = new JsonObject();
                        Log.d("feed" , "통신 실패");
                        t.printStackTrace(); // 에러 발생 원인 단계별로 출력
                    }
                }));
            }
        };

        handler.postDelayed(runnable, 1000);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void setData(){

        if(postData.size() == 0){
            recyclerView.setBackground( getActivity().getDrawable(R.drawable.no_post) );
        }
        adapter = new PostImageRVAdapter(getContext(), postData);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int gridPosition = position % 5;
                switch (gridPosition) {
                    case 0:
                    case 1:
                    case 2:
                        return 2;
                    case 3:
                    case 4:
                        return 3;
                }
                return 0;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    public void initScroll(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d("scroll" , layoutManager.findLastCompletelyVisibleItemPosition() +" | " + ( adapter.getItemCount()  -1 ) );
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition()== adapter.getItemCount() - 1) {
                        loadlayout.setVisibility(View.VISIBLE);
                    }
                    else
                        loadlayout.setVisibility(View.GONE);
                }
                else{
                    loadlayout.setVisibility(View.GONE);
                }
            }
        });

    }
}
