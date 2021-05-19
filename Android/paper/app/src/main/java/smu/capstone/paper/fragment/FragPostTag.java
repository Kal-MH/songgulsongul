package smu.capstone.paper.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.responseData.PostListResponse;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class FragPostTag extends Fragment {

    private View view;
    PostImageAdapter adapter;

    String keyword;
    GridView gridView;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    List<Post> tagData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_tag, container, false);
        gridView = view.findViewById(R.id.frag_tag_grid);

        //Activity에서 가져옴
        keyword = this.getArguments().getString("keyword");

        getTagData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), PostActivity.class);

                // 게시글 id 전달
                int postId = tagData.get(position).getId();
                intent.putExtra("post_id", postId);

                startActivity(intent);
                Log.d("TAG", position + "is Clicked" + postId);

            }
        });

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
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    tagData = result.getData();
                }
                else {
                    tagData = result.getData();
                }
                setTagData();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
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
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    tagData = result.getData();
                }
                else {
                    tagData = result.getData();
                }
                setTagData();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                tagData = null;
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setTagData(){
        if(tagData.size() == 0)
            gridView.setBackground( getActivity().getDrawable(R.drawable.no_post) );

        else
            gridView.setBackgroundColor(Color.argb(0,10,10,10));

        adapter = new PostImageAdapter(this.getContext(), R.layout.post_image_item, tagData);
        gridView.setAdapter(adapter);

    }
}
