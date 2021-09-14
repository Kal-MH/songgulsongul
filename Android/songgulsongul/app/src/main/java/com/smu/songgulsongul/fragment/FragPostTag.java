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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.smu.songgulsongul.R;
import com.smu.songgulsongul.recycler_adapter.PostImageRVAdapter;
import com.smu.songgulsongul.recycler_item.Post;
import com.smu.songgulsongul.data.post.Response.PostListResponse;
import com.smu.songgulsongul.server.RetrofitClient;
import com.smu.songgulsongul.server.ServiceApi;
import com.smu.songgulsongul.server.StatusCode;

public class FragPostTag extends Fragment {

    private View view;
    PostImageRVAdapter adapter;

    String keyword;
    RecyclerView recyclerView;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    List<Post> tagData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_tag, container, false);
        recyclerView = view.findViewById(R.id.frag_tag_grid);

        //Activity에서 가져옴
        keyword = this.getArguments().getString("keyword");

        getTagData();

        return view;
    }

    // server에서 data전달
    public void getTagData(){
       Log.d("search" , "FragPostTag--> " + keyword);

        serviceApi.SearchPostTag(keyword, 0 ).enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                PostListResponse result = response.body();
                int resultCode = result.getCode();
                if(resultCode == StatusCode.RESULT_SERVER_ERR){
                    Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == StatusCode.RESULT_OK){
                    tagData = result.getData();
                }
                else {
                    tagData = result.getData();
                }
                setTagData();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                tagData = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }
    // server에서 data전달
    public void getTagData(String query){
        Log.d("search" , "FragPostTag--> " + query);

        serviceApi.SearchPostTag(query, 0 ).enqueue(new Callback<PostListResponse>() {
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
                    tagData = result.getData();
                }
                else {
                    tagData = result.getData();
                }
                setTagData();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toasty.normal(getActivity(), "서버와의 통신이 불안정합니다.").show();
                tagData = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setTagData(){
        if(tagData.size() == 0)
            recyclerView.setBackground( getActivity().getDrawable(R.drawable.no_post) );

        else
            recyclerView.setBackgroundColor(Color.argb(0,10,10,10));


        adapter = new PostImageRVAdapter(getContext(), tagData);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }
}
