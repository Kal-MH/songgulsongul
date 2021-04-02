package smu.capstone.paper.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.DetectPaperActivity;
import smu.capstone.paper.activity.UploadDetailActivity;
import smu.capstone.paper.layout.CameraSurfaceView;

public class FragUploadCam extends Fragment {
    private int RESULT_PERMISSIONS=100;

    private  View view;
    private  Context context;
    CameraSurfaceView surfaceView;
    ImageView imageView;
    boolean isQuick;

    public FragUploadCam(boolean isQuick) {
        this.isQuick = isQuick;
    }

    @SuppressLint("WrongViewCast")

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        view = inflater.inflate(R.layout.frag_upload_cam, container, false);
        context = view.getContext();

        surfaceView = view.findViewById(R.id.upload_camview);

        ImageButton button = view.findViewById(R.id.frag_upload_cambtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });


        surfaceView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                surfaceView.camera.autoFocus (new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            Toast.makeText(context,"Auto Focus Success",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"Auto Focus Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        return view;
    }


    public void capture(){
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File tempFile;
                try {
                    tempFile = File.createTempFile("temp", null, context.getCacheDir());

                    OutputStream os = new FileOutputStream(tempFile);
                    os.write(data);
                    os.flush();
                    os.close();

                    String filePath= tempFile.getAbsolutePath();

                    if(isQuick){
                        Intent intent = new Intent(context, UploadDetailActivity.class);
                        intent.putExtra("path", filePath);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Intent intent = new Intent(context, DetectPaperActivity.class);
                        intent.putExtra("path", filePath);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch ( Exception ee){

                }



            }
        });
    }
}
