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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smu.capstone.paper.R;
import smu.capstone.paper.activity.PostActivity;
import smu.capstone.paper.adapter.PostImageAdapter;
import smu.capstone.paper.adapter.UserlistAdapter;
import smu.capstone.paper.server.RetrofitClient;
import smu.capstone.paper.server.ServiceApi;
import smu.capstone.paper.server.StatusCode;

public class FragPostAccount extends Fragment {

    private View view;
    UserlistAdapter adapter;

    ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    StatusCode statusCode;
    JsonObject accountData;

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

        serviceApi.SearchPost("id",keyword, 0 ).enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();

                int resultCode = result.get("code").getAsInt();
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    accountData = result;
                }
                else {
                    accountData = result;
                }
                setAccountData();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                accountData = new JsonObject();
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }
    // server에서 data전달
    public void getAccountData(String query){
        Log.d("search" , "FragPostTag--> " + query);

        serviceApi.SearchPost("id",query, 0 ).enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();

                int resultCode = result.get("code").getAsInt();
                if(resultCode == statusCode.RESULT_SERVER_ERR){
                    Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                    // 빈 화면 보여주지말고 무슨액션을 취해야할듯함!
                }
                else if( resultCode == statusCode.RESULT_OK){
                    accountData = result;
                }
                else {
                    accountData = result;
                }
                setAccountData();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                accountData = new JsonObject();
                Log.d("feed" , "통신 실패");
                t.printStackTrace(); // 에러 발생 원인 단계별로 출력
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAccountData(){
        if(accountData.get("data").getAsJsonArray().size() == 0)
            recyclerView.setBackground( getActivity().getDrawable(R.drawable.no_post) );

        else
            recyclerView.setBackgroundColor(Color.argb(0,10,10,10));

        adapter = new UserlistAdapter(this.getContext(),accountData);
        recyclerView.setAdapter(adapter);

    }

}
