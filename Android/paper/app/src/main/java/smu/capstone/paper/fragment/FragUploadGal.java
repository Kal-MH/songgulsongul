package smu.capstone.paper.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smu.capstone.paper.R;
import smu.capstone.paper.adapter.GalleryAdapter;
import smu.capstone.paper.item.GalleryItem;

public class FragUploadGal extends Fragment {


    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    ImageView picked;
    String picked_path;

    private View view;
    ArrayList<GalleryItem> items;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_upload_gal, container, false);
        picked = view.findViewById(R.id.upload_gal_pick);


        items = getPathOfAllImages();


        recyclerView = (RecyclerView) view.findViewById(R.id.upload_gal_grid);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList<GalleryItem> imageUrlList = getPathOfAllImages();
        GalleryAdapter galleryAdapter = new GalleryAdapter(view.getContext(), imageUrlList);
        recyclerView.setAdapter(galleryAdapter);

        //첫 사진을 메인으로띄움
        Glide.with(view.getContext()).load(imageUrlList.get(0).getPath()).into(picked);
        picked_path = imageUrlList.get(0).getPath();


        //선택시 선택사진으로 전환환다
        galleryAdapter.setWhenClickListener(new GalleryAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(GalleryItem galleryItem) {
                Glide.with(view.getContext()).load(galleryItem.getPath()).into(picked);
                picked_path = galleryItem.getPath();
            }
        });



        return view;
    }

    private ArrayList<GalleryItem> getPathOfAllImages() {
        ArrayList<GalleryItem> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        int lastIndex;
        while (cursor.moveToNext())
        {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                result.add(new GalleryItem(absolutePathOfImage));
            }
        }

        // 로그에 찍어보겠습니다.//..
        Log.d("TAG" ,"PhotoSelectActivity.java | getPathOfAllImages");
        for (GalleryItem g : result)
            Log.d("TAG" ,"absolute Path of Image |" + g.getPath() + "|");

        return result;
    }

    public String getPicked_path(){
        return picked_path;
    }


}