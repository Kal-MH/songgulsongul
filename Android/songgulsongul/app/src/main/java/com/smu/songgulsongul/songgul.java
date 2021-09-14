package com.smu.songgulsongul;

import android.app.Application;

import org.opencv.core.Mat;

public class songgul extends Application {

    private Mat sourceMat;
    private Mat paperMat;
    private Mat croppedMat;
    private Mat preEditingMat;
    private Mat editingMat;

    @Override
    public void onCreate() {
        //변수 초기화
        super.onCreate();
    }


    public void setPaperMat(Mat mat) {
        if (paperMat != null)
            if (!paperMat.empty())
                paperMat.release();
        paperMat = mat;
    }

    public Mat getPaperMat() {
        return paperMat;
    }

    public void setCroppedMat(Mat mat) {
        if (croppedMat != null)
            if (!croppedMat.empty())
                croppedMat.release();
        croppedMat = mat;
    }

    public Mat getCroppedMat() {
        return croppedMat;
    }

    public void setEditingMat(Mat mat) {
        if (editingMat != null)
            if (!editingMat.empty())
                editingMat.release();
        editingMat = mat;
    }

    public Mat getEditingMat() {
        return editingMat;
    }


    public void releaseAllMat() {
        if (sourceMat != null)
            if (!sourceMat.empty())
                sourceMat.release();

        if (paperMat != null)
            if (!paperMat.empty())
                paperMat.release();

        if (croppedMat != null)
            if (!croppedMat.empty())
                croppedMat.release();

        if (preEditingMat != null)
            if (!preEditingMat.empty())
                preEditingMat.release();

        if (editingMat != null)
            if (!editingMat.empty())
                editingMat.release();
    }


    @Override
    public void onTerminate() {
        //프로세스 소멸 시 호출
        super.onTerminate();
    }

}
