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

        //선택시 선택사진으로 전환환다
        galleryAdapter.setWhenClickListener(new GalleryAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(GalleryItem galleryItem) {
                Glide.with(view.getContext()).load(galleryItem.getPath()).into(picked);
            }
        });



        return view;
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            // 사진을 선택하고 왔을 때만 처리한다.
            if(resultCode == RESULT_OK){
                // 선택한 이미지를 지칭하는 Uri 객체를 얻어온다.
                Uri uri = data.getData();
                // Uri 객체를 통해서 컨텐츠 프로바이더를 통해 이미지의 정보를 가져온다.
                ContentResolver resolver = getActivity().getContentResolver();
                Cursor cursor = resolver.query(uri, null, null, null, null);
                cursor.moveToNext();

                // 사용자가 선택한 이미지의 경로 데이터를 가져온다.
                int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String source = cursor.getString(index);

                // 경로 데이터를 통해서 이미지 객체를 생성한다
                Bitmap bitmap = BitmapFactory.decodeFile(source);

                // 이미지의 크기를 조정한다.
                Bitmap bitmap2 = resizeBitmap(1024, bitmap);

                // 회전 각도 값을 가져온다.
                float degree = getDegree(source);
                Bitmap bitmap3 = rotateBitmap(bitmap2, degree);

//                fragUploadImg.setImageBitmap(bitmap3);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
*/

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




}