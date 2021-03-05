package smu.capstone.paper.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import smu.capstone.paper.R;
import smu.capstone.paper.activity.EditActivity;
import smu.capstone.paper.layout.CameraSurfaceView;

public class FragUploadCam extends Fragment {
    private int RESULT_PERMISSIONS=100;

    private  View view;
    private  Context context;
    CameraSurfaceView surfaceView;
    ImageView imageView;

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

                File outputDir = context.getCacheDir();
                File tempFile = new File(outputDir, "temp.dat");
                try {
                    OutputStream os = new FileOutputStream(tempFile);
                    os.write(data);
                    os.flush();
                    os.close();
                } catch (Exception e) { Log.e(context.getClass().getSimpleName(), "Error writing file", e);}

                String filePath= tempFile.getAbsolutePath();
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);

            }
        });
    }
}
