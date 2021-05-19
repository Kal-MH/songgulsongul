package smu.capstone.paper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.activity.PostSearchActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.responseData.Post;
import smu.capstone.paper.responseData.PostListResponse;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class FragHomeComu extends Fragment {
    private View view;
    private SearchView searchView;
    GridView gridView;
    SwipeRefreshLayout swipeRefreshLayout;

    PostImageAdapter adapter;
    List<Post> postData;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home_comu, container, false);

        // id 세팅
        gridView = view.findViewById(R.id.comu_grid);
        searchView = view.findViewById(R.id.comu_search);
        swipeRefreshLayout = view.findViewById(R.id.comu_refresh_layout);

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
        //Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getContext(), PostActivity.class);

                // 게시글 id 전달
                int postId = postData.get(position).getId();
                intent.putExtra("post_id", postId);

                startActivity(intent);
                Log.d("TAG", position + "is Clicked");
            }
        });



        return view;
    }

    //server에서 data전달
    public void GetCommunityData(){
        serviceApi.GetCommunity(20).enqueue((new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                PostListResponse result = response.body();

                int resultCode = result.getCode();
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    postData = result.getData();
                }
                else {
                    postData = result.getData();
                }

                setData();
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
              //  feeds = new JsonObject();
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        }));
    }

    public  void setData(){
        // 어뎁터 적용
        adapter = new PostImageAdapter(this.getContext(), R.layout.post_image_item, postData);
        gridView.setAdapter(adapter);
    }

}
