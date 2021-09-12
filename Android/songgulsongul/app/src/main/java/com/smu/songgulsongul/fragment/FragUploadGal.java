package com.smu.songgulsongul.fragment;

import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.smu.songgulsongul.R;
import com.smu.songgulsongul.adapter.GalleryAdapter;
import com.smu.songgulsongul.item.GalleryItem;

public class FragUploadGal extends Fragment {


    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    ImageView picked;
    String picked_path;

    private View view;
    GalleryAdapter galleryAdapter;

    boolean isLoading = false;
    Cursor cursor;
    int columnIndex;
    int columnDisplayname;
    int offset = 12;


    Display display;
    Point point = new Point();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        display = getContext().getDisplay();
        display.getRealSize(point);

        view = inflater.inflate(R.layout.frag_upload_gal, container, false);
        picked = view.findViewById(R.id.upload_gal_pick);
        recyclerView = (RecyclerView) view.findViewById(R.id.upload_gal_grid);

        gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        List<GalleryItem> imageUrlList = getPathOfAllImages();

        galleryAdapter = new GalleryAdapter(view.getContext(), imageUrlList);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(galleryAdapter);

        initScroll();
        if(!imageUrlList.isEmpty()){
            //첫 사진을 메인으로띄움
            Glide.with(view.getContext()).load(imageUrlList.get(0).getPath()).into(picked);
            picked_path = imageUrlList.get(0).getPath();
        }


        //선택시 선택사진으로 전환환다
        galleryAdapter.setWhenClickListener(new GalleryAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(GalleryItem galleryItem) {
                Glide.with(view.getContext()).load(galleryItem.getPath()).into(picked);
                picked_path = galleryItem.getPath();
                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(point.x,point.y/3));
                recyclerView.requestLayout();
            }
        });



        return view;
    }

    private List<GalleryItem> getPathOfAllImages() {
        List<GalleryItem> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };

        cursor = getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        int count = 0;
        while (cursor.moveToNext())
        {
            count++;
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                result.add(new GalleryItem(absolutePathOfImage, nameOfFile));
            }
            if( count >= offset)
                break;
        }

        return result;
    }

    private void getPathOfAllImagesMore() {
        final List<GalleryItem> result = new ArrayList<>();

        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                int count = 0;
                while (cursor.moveToNext())
                {
                    count++;
                    String absolutePathOfImage = cursor.getString(columnIndex);
                    String nameOfFile = cursor.getString(columnDisplayname);

                    if (!TextUtils.isEmpty(absolutePathOfImage))
                    {
                        result.add(new GalleryItem(absolutePathOfImage, nameOfFile));
                    }
                    if( count >= offset) {
                        break;
                    }
                }
                if( galleryAdapter.addItem(result))
                    galleryAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public String getPicked_path(){
        return picked_path;
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

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == galleryAdapter.getItemCount() - 1) {
                         isLoading = true;
                        getPathOfAllImagesMore();
                    }
                }
                if(dy>10){
                    recyclerView.setLayoutParams(new LinearLayout.LayoutParams(point.x,point.y/3*2));
                    recyclerView.requestLayout();
                }
            }
        });

    }

}