#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <math.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>

using namespace cv;
using namespace std;

static const int smallSizeX = 500;
static const int smallSizeY = 500;

int smallestColor(int x, int y, int z) {

    int smallest = 256;

    if (x < smallest)
        smallest=x;
    if (y < smallest)
        smallest=y;
    if(z < smallest)
        smallest=z;

    return smallest;
}
char smallestColor(char x, char y, char z) {

    char smallest = 256;

    if (x < smallest)
        smallest=x;
    if (y < smallest)
        smallest=y;
    if(z < smallest)
        smallest=z;

    return smallest;
}

void RGB2MinGray(Mat img, Mat output){

    //TODO: 포인터 사용 안하면 작동 안하나봄. 고칠것

    int width = img.cols;
    int height = img.rows;
    int grayScale = 0;
    Mat loc(height, width, CV_8UC3, Scalar(0, 0, 0));
    for (int i = 0; i < height; i++){
        for (int k = 0; k < width; k++){

            grayScale = smallestColor(img.at<Vec3b>(i, k)[0],img.at<Vec3b>(i, k)[1],img.at<Vec3b>(i, k)[2]);

            loc.at<Vec3b>(i, k)[0] = grayScale;
            loc.at<Vec3b>(i, k)[1] = grayScale;
            loc.at<Vec3b>(i, k)[2] = grayScale;
        }
    }
    output = loc;
}

//water filling
//https://github.com/seungjun45/Water-Filling

float progressToValue(int currentProgress,float on0, float on100){
    float alpha = currentProgress*0.01;
    return on0 * (1 - alpha) + on100 * alpha;
}
float progressToValue(int currentProgress,float on0, float on100, float on50){
    float alpha;
    if(currentProgress<50){
        alpha = currentProgress*0.02;
        return on0 * (1 - alpha) + on50 * alpha;
    }
    else if(currentProgress==50)
        return on50;
    alpha = (currentProgress-50)*0.02;
    return on50 * (1 - alpha) + on100 * alpha;
}


Mat RGB2YCbCr(Mat img){

    int width = img.cols;
    int height = img.rows;
    float dummy;
    Mat img_copy(height, width, CV_8UC3, Scalar(0, 0, 0));

    for (int i = 0; i < height; i++){
        for (int k = 0; k < width; k++){ // indexing every channels of a 2-D array form of images.
            /* Y = 0.299*imImg_R + 0.587*imImg_G + 0.114*imImg_B;
            Cb = 0.564*(imImg_B - Y);
            Cr = 0.713*(imImg_R - Y);*/

            //assigning Y values
            dummy = round(16+65.481/255.0*float(img.at<Vec3b>(i, k)[2]) + 128.553/255.0*float(img.at<Vec3b>(i, k)[1]) + 24.966/255.0*float(img.at<Vec3b>(i, k)[0]));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[2] = dummy;


            //assinging Cb
            dummy = round(128 - 37.797 / 255.0 * float(img.at<Vec3b>(i, k)[2]) - 74.203 / 255.0 * float(img.at<Vec3b>(i, k)[1]) + 112.0 / 255.0 * float(img.at<Vec3b>(i, k)[0]));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[1] = dummy;

            //assigning Cr
            dummy = round(128 + 112.0 / 255 * float(img.at<Vec3b>(i, k)[2]) - 93.786 / 255.0 * float(img.at<Vec3b>(i, k)[1]) - 18.214 / 255.0 * float(img.at<Vec3b>(i, k)[0]));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[0] = dummy;



        }
    }

    return img_copy;

}
void ResizeToCustom(Mat &img, Mat &out, int targetPixelSize){
    int width = img.cols;
    int height = img.rows;



    if(width>height){
        if(width>targetPixelSize){
            resize(img,out,Size(targetPixelSize,int(height*targetPixelSize/(float)width)),0, 0, INTER_LINEAR);
            return;
        }
    }
    if(height>targetPixelSize){
        resize(img,out,Size(int(width*targetPixelSize/(float)height),targetPixelSize),0, 0, INTER_LINEAR);
        return;
    }

    if(img.data != out.data){
        out.release();
        out = img.clone();
    }

    return;
}

void ResizeTo2048(Mat &img, Mat &out){
    int width = img.cols;
    int height = img.rows;



    if(width>height){
        if(width>2048){
            resize(img,out,Size(2048,int(height*2048.0/width)),0, 0, INTER_LINEAR);
            return;
        }
    }
    if(height>2048){
        resize(img,out,Size(int(width*2048.0/height),2048),0, 0, INTER_LINEAR);
        return;
    }

    if(img.data != out.data){
        out.release();
        out = img.clone();
    }

    return;
}
Size ResizeTo2048(Size originalSize){
    int width = originalSize.width;
    int height = originalSize.height;

    Mat outImg;
    float sizeValue = 1.0f;

    if(width>height){
        if(width>2048){
            return Size(2048,int(height*2048.0/width));
        }
    }
    if(height>2048){
        return Size(int(height*2048.0/height),2048);
    }

    return originalSize;
}


Mat YCbCr2RGB(Mat img){

    int width = img.cols;
    int height = img.rows;
    float dummy;

    Mat img_copy(height, width, CV_8UC3, Scalar(0, 0, 0));

    for (int i = 0; i < height; i++){
        for (int k = 0; k < width; k++){ // indexing every channels of a 2-D array form of images.
            /* Y = 0.299*imImg_R + 0.587*imImg_G + 0.114*imImg_B;
            Cb = 0.564*(imImg_B - Y);
            Cr = 0.713*(imImg_R - Y);*/

            //assigning R values
            dummy = round(1.1644 * float(img.at<Vec3b>(i, k)[2]-16) +1.5960 * (float(img.at<Vec3b>(i, k)[0])-128));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[2] = dummy;


            //assinging G
            dummy = round(1.1644 * (float(img.at<Vec3b>(i, k)[2]) - 16) - 0.3918 * (float(img.at<Vec3b>(i, k)[1]) - 128) - 0.8130*(float(img.at<Vec3b>(i, k)[0]) - 128));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[1] = dummy;

            //assigning B
            dummy = round(1.1644 * (float(img.at<Vec3b>(i, k)[2]) - 16) + 2.0172* (float(img.at<Vec3b>(i, k)[1]) - 128));
            if (dummy < 0){
                dummy = 0;
            }
            if (dummy > 255){
                dummy = 255;
            }
            img_copy.at<Vec3b>(i, k)[0] = dummy;



        }
    }

    return img_copy;

}

float inv_relu(float input_){
    float output_;
    if (input_ > 0){
        output_=0;
    }
    else{
        output_ = input_;
    }
    return output_;
}

Mat water_filling(Mat input_){


    input_.convertTo(input_, CV_32F);
    Mat h_;
    Size size(0, 0);
    resize(input_, h_, size, 0.2, 0.2, INTER_LINEAR);

    int height = h_.rows;
    int width = h_.cols;
    Mat w_ = Mat(h_.rows, h_.cols, CV_32F, Scalar(0, 0, 0));
    Mat G_ = Mat(h_.rows, h_.cols, CV_32F, Scalar(0, 0, 0));


    double w_pre;
    double pouring;
    double neta = 0.2;
    double G_peak;
    double G_min;
    float* w_ptr = (float*)w_.data;
    float* G_ptr = (float*)G_.data;
    size_t elem_step = w_.step / sizeof(float);
    float temp;

    double del_w;

    for (int t = 0; t < 2500; t++){
        G_ = w_ + h_;
        minMaxLoc(G_, &G_min, &G_peak);
        for (int y = 1; y < (height-2); y++){

            for (int x = 1; x < (width-2); x++){

                w_pre = w_ptr[x+y*elem_step];
                pouring = exp(-t)*(G_peak - G_ptr[x+y*elem_step]);
                del_w = neta*(inv_relu(-G_ptr[x + y*elem_step] + G_ptr[x + (y + 1)*elem_step])
                              + inv_relu(-G_ptr[x + y*elem_step] + G_ptr[x + (y - 1)*elem_step])
                              + inv_relu(-G_ptr[x + y*elem_step] + G_ptr[(x + 1) + y*elem_step])
                              + inv_relu(-G_ptr[x + y*elem_step] + G_ptr[(x - 1) + y*elem_step]));
                temp = del_w + pouring + w_pre;
                if (temp < 0){
                    w_ptr[x + y*elem_step] = 0;
                }
                else{
                    w_ptr[x + y*elem_step] = temp;
                }
                //printf("w_pre is %f\n", w_pre);
                //printf("pouring is %f\n", pouring);
                //printf("del_w is %f\n", del_w);


            }
        }

    }
    Mat output_;
    Size outsize(input_.cols, input_.rows);
    resize(G_, output_, outsize, 0, 0, INTER_LINEAR);
    //output_ = input_ / output_*255;
    output_.convertTo(output_, CV_8UC1);
    return output_;
}

Mat incre_filling(Mat h_, Mat Original){


    h_.convertTo(h_, CV_32F);
    Original.convertTo(Original, CV_32F);

    int height = h_.rows;
    int width = h_.cols;
    Mat w_ = Mat(h_.rows, h_.cols, CV_32F, Scalar(0, 0, 0));
    Mat G_ = Mat(h_.rows, h_.cols, CV_32F, Scalar(0, 0, 0));


    double w_pre;
    double pouring;
    double neta = 0.2;
    double G_peak;
    double G_min;
    float* w_ptr = (float*)w_.data;
    float* G_ptr = (float*)G_.data;
    size_t elem_step = w_.step / sizeof(float);
    float temp;

    double del_w;

    for (int t = 0; t < 100; t++){
        G_ = w_ + h_;
        for (int y = 1; y < (height - 2); y++){

            for (int x = 1; x < (width - 2); x++){

                w_pre = w_ptr[x + y*elem_step];
                del_w = neta*(-G_ptr[x + y*elem_step] + G_ptr[x + (y + 1)*elem_step]
                              + -G_ptr[x + y*elem_step] + G_ptr[x + (y - 1)*elem_step]
                              + -G_ptr[x + y*elem_step] + G_ptr[(x + 1) + y*elem_step]
                              + -G_ptr[x + y*elem_step] + G_ptr[(x - 1) + y*elem_step]);
                temp = del_w + w_pre;
                if (temp < 0){
                    w_ptr[x + y*elem_step] = 0;
                }
                else{
                    w_ptr[x + y*elem_step] = temp;
                }
                //printf("w_pre is %f\n", w_pre);
                //printf("pouring is %f\n", pouring);
                //if (t == 99){
                //	printf("del_w is %f\n", del_w);
                //}


            }
        }

    }
    Mat output_;
    output_ = 0.85*Original / G_ * 255;
    output_.convertTo(output_, CV_8UC1);
    return output_;
}

Mat getImageCannyBorders( Mat src, int th1 = 25, int th2 = 200)
{
    Mat border;
    Mat resized;

    /// Convert it to gray
    border =  Mat();
    //cvtColor(src, border, COLOR_RGB2GRAY );
    cvtColor(src, border, COLOR_BGR2GRAY ); //TODO: BRG or RGB 테스트
    //cvtColor( src, border, COLOR_RGB2GRAY );
    GaussianBlur( border, border, Size(5,5), 0, 0, BORDER_DEFAULT );
    // Canny edge detector
    Canny(border, border, th1, th2);
    return border;
}
vector<Point> findBordersPoints(Mat src)
{
    vector<vector<Point> > contours;	/// Find contours
    findContours( src, contours, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0) );

    vector<vector<Point>> contours_poly( contours.size() );

    for(unsigned int i = 0; i < contours.size(); i++ )
    {
        double peri = arcLength(contours[i], true);
        approxPolyDP( Mat(contours[i]), contours_poly[i], 0.02 * peri, true );
        if (contours_poly[i].size() == 4)
        {
            return contours_poly[i];
        }
    }

    return contours_poly[0];
}

//종이 모서리 순서 정리
vector<Point> orderPoints(vector<Point> points)
{
    vector<Point> order;
    int maxWidth = max(max(points[0].x, points[1].x), max(points[2].x,points[3].x));
    int maxHeight = max(max(points[0].y, points[1].y), max(points[2].y,points[3].y));

    //Point tl(std::numeric_limits<int>::max(), 0);
    //Point tr(0,std::numeric_limits<int>::max());
    //Point bl(std::numeric_limits<int>::max(),std::numeric_limits<int>::max());
    //Point br(std::numeric_limits<int>::min(), std::numeric_limits<int>::min());
    Point tl(maxWidth, maxHeight);
    Point tr(maxWidth, maxHeight);
    Point bl(0, 0);
    Point br(0, 0);


    for (int i = 0; i < 4;i++)
    {
        int x = points[i].x;
        int y = points[i].y;

        if (x + y <= tl.x + tl.y)
            tl = points[i];
        if (y - x <= tr.y - tr.x)
            tr = points[i];
        if (y - x >= bl.y - bl.x)
            bl = points[i];
        if (x + y >= br.x + br.y)
            br = points[i];
    }
    order.push_back(tl);
    order.push_back(tr);
    order.push_back(bl);
    order.push_back(br);
    return order;
}

//종이 변환
Mat fourPointsTransform(Mat src, vector<Point> corners)
{

    Point2f points[4];

    vector<Point> pv = orderPoints(corners);
    std::copy(pv.begin(), pv.end(), points);
    Point tl = points[0];
    Point tr = points[1];
    Point bl = points[2];
    Point br = points[3];


    // compute the width of the new image, which will be the
    // maximum distance between bottom-right and bottom-left
    // x-coordiates or the top-right and top-left x-coordinates
    float widthA, widthB, maxWidth;
    float heightA, heightB, maxHeight;

    widthA = sqrt((pow((br.x - bl.x), 2)) + (pow((br.y - bl.y), 2)));
    widthB = sqrt((pow((tr.x - tl.x), 2)) + (pow((tr.y - tl.y), 2)));
    maxWidth = max(int(widthA), int(widthB));

    // compute the height of the new image, which will be the
    // maximum distance between the top-right and bottom-right
    // y-coordinates or the top-left and bottom-left y-coordinates
    heightA = sqrt((pow((tr.x - br.x), 2)) + (pow((tr.y - br.y), 2)));
    heightB = sqrt((pow((tl.x - bl.x), 2)) + (pow((tl.y - bl.y), 2)));
    maxHeight = max(int(heightA), int(heightB));

    // now that we have the dimensions of the new image, construct
    // the set of destination points to obtain a "birds eye view",
    // (i.e. top-down view) of the image, again specifying points
    // in the top-left, top-right, bottom-right, and bottom-left order
    Point2f dts[4];
    dts[0] = Point(0,0);
    dts[1] = Point(maxWidth-1,0);
    dts[2] = Point(0, maxHeight-1);
    dts[3] = Point(maxWidth-1,maxHeight-1);

    Mat warpMatrix = getPerspectiveTransform(points, dts);

    Mat rotated;
    //Size size(int(max(maxWidth, 1.0f)), int(max(maxHeight, 1.0f)));
    Size size(maxWidth, maxHeight);

    warpPerspective(src, rotated, warpMatrix, size, INTER_LINEAR, BORDER_CONSTANT);
    return rotated;
    /**/
    return src;
}

//작게처리했던 종이이미지 기준으로 찾아낸 모서리를 다시 원본비율로 바꿀 수 있는 함수
vector<Point> ResizePoints(vector<Point> pointsInput, float scaleX, float scaleY)
{
    vector<Point> scaledPoints;
    //scaledPoints.resize(pointsInput.size());
    //std::copy(pointsInput.begin(),pointsInput.end(),scaledPoints);

    for(Point p : pointsInput)
    {
        Point scaledPoint = Point(p.x*scaleX,p.y*scaleY);
        scaledPoints.push_back(scaledPoint);
    }

    return scaledPoints;
}






extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_DetectPaperActivity_PaperProcessing(JNIEnv *env, jobject thiz,
                                                                     jlong input_image,
                                                                     jlong output_image,
                                                                     jlong input_points,jint offset_x,jint offset_y, jfloat scale_factor, jint th1,
                                                                     jint th2) {
    Mat &img_input = *(Mat *) input_image;

    Mat &img_output = *(Mat *) output_image;

    Mat &point_input = *(Mat *) input_points;

    vector<Point> borders;
    vector<Point> bordersResized;

    Mat smallImg;


    if(input_points == NULL)
    {

        /*
        resize(img_input, smallImg,Size(smallSizeX,smallSizeY),0,0);

        //img_output = getImageCannyBorders(img_input, th1, th2);
        img_output = getImageCannyBorders(smallImg, th1, th2);

        //img_output2 = getImageCannyBorders(smallImg, th1, th2);

        borders = findBordersPoints(img_output);

        bordersResized = ResizePoints(borders,img_input.cols/smallSizeX, img_input.rows/smallSizeY);
        */
    }
    else
    {
        //하나하나 처리하니 비효율적이고 오류가 있음
        /*
        for (int x = 0; x < point_input.cols; x++){
            for (int y = 0; y < point_input.rows; y++)
                borders2.push_back(Point());
        }*/

        bordersResized = (vector<Point>) point_input;


        borders = ResizePoints(bordersResized,(float)smallSizeX/img_input.cols,(float)smallSizeY/img_input.rows);

        resize(img_input, smallImg,Size(smallSizeX,smallSizeY),0,0);

        //img_output = getImageCannyBorders(img_input, th1, th2);

        img_output.release();//TODO input이랑 output이 같을경우 위험
        img_output = smallImg;

    }


    /*
    if (img_output.type()==CV_8UC1) {
        //input image is grayscale
        cvtColor(img_output, img_output, COLOR_GRAY2BGR);
    }*/

    for(Point p : bordersResized)
    {
        Scalar colorP(0,255,0); // Green
        //circle(img_output,p,3, colorP,10);
        //디버그용 점 찍는 작업
    }




    //Mat ticketImage = fourPointsTransform(img_input, borders);

    //img_output = ticketImage;
    //img_output2 = fourPointsTransform(img_input, borders);

    //img_output2 = fourPointsTransform(img_output2, bordersResized);
    if(borders.size()==4){
        img_output = fourPointsTransform(img_input, bordersResized);

        /*
        img_output2 = fourPointsTransform(smallImg, borders);



        Mat img_YCbCr = RGB2YCbCr(img_output2);
        //vector<int> compression_params; //vector that stores the compression parameters of the image
        Mat chan[3];
        split(img_YCbCr, chan);
        //compression_params.push_back(CV_IMWRITE_JPEG_QUALITY); //specify the compression technique
        //compression_params.push_back(0); //specify the compression quality

        Mat G_ = water_filling(chan[2]);
        G_ = incre_filling(G_, chan[2]);

        vector<Mat> channels_(3);
        channels_[0] = chan[0];
        channels_[1] = chan[1];
        channels_[2] = G_;
        Mat img_YCbCr_rec;
        merge(channels_, img_YCbCr_rec);
        Mat rec_img = YCbCr2RGB(img_YCbCr_rec);
        img_output2 = rec_img;

        */
    }
    else{
        img_output = img_input;
    }
    //cvtColor( img_input, img_output, COLOR_RGB2GRAY);
    //cvtColor( ticketImage, graysrc, CV_BGR2GRAY );

    //threshold(graysrc,ticketImage, th1, th2,THRESH_TOZERO);

    //blur( img_output, img_output, Size(5,5) );

    //Canny( img_output, img_output, th1, th2);



}extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_DetectPaperActivity_GetPaperPoints(JNIEnv *env, jobject thiz,
                                                                    jlong input_image,
                                                                    jlong output_point, jint th1,
                                                                    jint th2) {
    Mat &img_input = *(Mat *) input_image;

    Mat &points = *(Mat *) output_point;

    Mat imgCanny;

    Mat smallImg;



    resize(img_input, smallImg,Size(smallSizeX,smallSizeY),0,0);

    //img_output = getImageCannyBorders(img_input, th1, th2);
    imgCanny = getImageCannyBorders(smallImg, th1, th2);

    //img_output2 = getImageCannyBorders(smallImg, th1, th2);

    vector<Point> borders = findBordersPoints(imgCanny);

    borders = orderPoints(borders);

    vector<Point> bordersResized = ResizePoints(borders,img_input.cols/smallSizeX, img_input.rows/smallSizeY);

    points = Mat(bordersResized, true); //points 출력
    //jobjectArray result = env->NewObjectArray(4,env->FindClass("org/opencv/core/Point"),NULL);
    /*
    jobjectArray result = NULL;
    for (int k = 0; k < bordersResized.size(); k++) {
        jintArray element = env->NewIntArray(2);
        if (element == NULL)
            break;
        jint buf[2] = { bordersResized[k].x, bordersResized[k].y };
        env->SetIntArrayRegion(element, 0, 2, buf);
        //env->SetObjectArrayElement(result, k, element);
        env->SetObjectArrayElement(result, k ,element);
    }
    return result;//값 하나하나 복사하면 너무 비효율적
     //메모리 누수 위험
     //이상하게 include 쪽에서 오류남
    */
    imgCanny.release();
    smallImg.release();
}extern "C"
JNIEXPORT jintArray JNICALL
Java_com_smu_songgulsongul_activity_DetectPicActivity_DetectPic(JNIEnv *env, jobject thiz,
                                                             jlong img_input,
                                                             jint th1, jint th2) {
    Mat &imgInput = *(Mat *) img_input;



    Mat sizedImage;

    Mat borders;


    double area;
    Rect rect;
    RotatedRect rotatedRect;

    vector<Point> pointsFromBox;

    resize(imgInput, sizedImage,Size(smallSizeX,smallSizeY),0,0);

    borders = getImageCannyBorders(sizedImage, th1, th2);

    vector<vector<Point>> contours;	/// Find contours
    findContours( borders, contours, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0) );

    vector<vector<Point>> contours_poly( contours.size() );


    for(unsigned int i = 0; i < contours.size(); i++ )
    {
        /*
        double peri = arcLength(contours[i], true);
        approxPolyDP( Mat(contours[i]), contours_poly[i], 0.02 * peri, true );
        if (contours_poly[i].size() == 4)
        {
            return contours_poly[i];
        }*/
        area = contourArea(contours[i]);
        if(area > 20){
            //rotatedRect = minAreaRect(contours[i]);
             rect = boundingRect(contours[i]);
            //boxPoints(rect, pointsFromBox);
            break;
        }
    }


    /*
    rotatedRect.center.x *= imgInput.cols/smallSizeX;
    rotatedRect.center.y *= imgInput.rows/smallSizeY;
    rotatedRect.size.height*= imgInput.cols/smallSizeX;
    rotatedRect.size.width*= imgInput.rows/smallSizeY;
    */
    jintArray rectFromJava = (env)->NewIntArray(4);

    jint *ptrArray = env->GetIntArrayElements(rectFromJava, 0);
    /*
    ptrArray[0] = rotatedRect.center.x - rotatedRect.size.width/2; //left
    ptrArray[1] = rotatedRect.center.y - rotatedRect.size.height/2; //top
    ptrArray[2] = rotatedRect.center.x + rotatedRect.size.width/2; //right
    ptrArray[3] = rotatedRect.center.y + rotatedRect.size.height/2; //bottom
     */
    ptrArray[0] = rect.x;
    ptrArray[1] = rect.y;
    ptrArray[2] = rect.x + rect.width;
    ptrArray[3] = rect.y + rect.height;
    env->ReleaseIntArrayElements(rectFromJava, ptrArray, 0);


    sizedImage.release();
    borders.release();

    return rectFromJava;

    /*
    if(!pointsFromBox.empty()){
        points = Mat(ResizePoints(pointsFromBox,imgInput.cols/smallSizeX, imgInput.rows/smallSizeY), true);
    }*/

}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageRatioActivity_changeImageRatio(JNIEnv *env, jobject thiz,
                                                                         jlong input_img_address,
                                                                         jlong output_img_address,
                                                                         jint seek_bar_progress) {
    Mat &imgInput = *(Mat *) input_img_address;
    Mat &img_output = *(Mat *) output_img_address;



    Size targetSize;
    if(seek_bar_progress == 0){
        targetSize = Size(imgInput.cols*(1.5),imgInput.rows*(0.5));
    }
    else{
        targetSize = Size(imgInput.cols*(1-((seek_bar_progress/100.0f)-0.5)),imgInput.rows*(1+((seek_bar_progress/100.0f)-0.5)));
    }
    //targetSize = ResizeTo2048(targetSize);
    resize(imgInput, img_output,targetSize,0,0,INTER_LINEAR);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageHistogramActivity_equalizeHistogram(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jlong input_img_address,
                                                                              jlong output_img_address,
                                                                              jint alpha) {
    Mat &imgInput = *(Mat *) input_img_address;
    Mat &img_output = *(Mat *) output_img_address;
    //equalizeHist(imgInput,img_output);

    Mat ycrcb= imgInput.clone();

    float f_alpha = alpha/100.0;
    float f_beta = 1.0-f_alpha;

    cvtColor(imgInput,ycrcb,COLOR_RGB2YCrCb);//BGR 대신 RGB

    vector<Mat> channels;
    split(ycrcb,channels);

    equalizeHist(channels[0], channels[0]);

    Mat result;
    merge(channels,ycrcb);

    cvtColor(ycrcb,result,COLOR_YCrCb2RGB);

    addWeighted(imgInput,f_beta,result,f_alpha,0,result);

    img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = result;
    ycrcb.release();

}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageHistogramActivity_equalizeHistogramClahe(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jlong input_img_address,
                                                                                   jlong output_img_address,
                                                                                   jint alpha) {
    Mat &imgInput = *(Mat *) input_img_address;
    Mat &img_output = *(Mat *) output_img_address;

    //Mat img_clahe;
    Mat ycrcb= imgInput.clone();

    float f_alpha = alpha/100.0;
    float f_beta = 1.0-f_alpha;


    cvtColor(imgInput,ycrcb,COLOR_RGB2YCrCb);//BGR 대신 RGB

    vector<Mat> channels;
    split(ycrcb,channels);

    Ptr<CLAHE> clahe = createCLAHE();
    clahe->setClipLimit(20);
    clahe->setTilesGridSize(Size(8,8));

    clahe->apply(channels[0],channels[0]);
    Mat result;
    merge(channels,ycrcb);

    cvtColor(ycrcb,result,COLOR_YCrCb2RGB);


    addWeighted(imgInput,f_beta,result,f_alpha,0,result);

    img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = result;
    ycrcb.release();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageColorActivity_setColors(JNIEnv *env, jobject thiz,
                                                                  jlong input_image_address,
                                                                  jlong output_image_address,
                                                                  jint hue_progress,
                                                                  jint saturation_progress,
                                                                  jint brightness_progress,
                                                                  jint contrast_progress) {
    // TODO: 알파채널 호완성 업데이트 할것
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;

    Mat locMat = imgInput.clone();



    int hue_shift = progressToValue(hue_progress,-90,90,0);
    int saturation = progressToValue(saturation_progress,-255,255,0);
    int value = progressToValue(brightness_progress,-255,255,0);


    //cvtColor(locMat,locMat,COLOR_BGR2HSV);
    cvtColor(locMat,locMat,COLOR_RGB2HSV);
    for (int j = 0; j < locMat.rows; j++)
    {
        for (int i = 0; i < locMat.cols; i++)
        {

            //색상(Hue)


            unsigned char h = locMat.at<Vec3b>(j, i)[0];
            if (h + hue_shift >= 180)
                h = (h + hue_shift) - 180;
            else if(h + hue_shift < 0){
                h = (h + hue_shift) + 180;
            }
            else
                h = h + hue_shift;
            locMat.at<Vec3b>(j, i)[0] = h;


            //채도(Saturation)
            unsigned char s = locMat.at<Vec3b>(j, i)[1];
            if (s + saturation >= 256)
                s = 255;
            else if(s + saturation < 0){
                s = 0;
            }
            else
                s = s + saturation;
            locMat.at<Vec3b>(j, i)[1] = s;

            //밝기(Value(Brightness))
            unsigned char v = locMat.at<Vec3b>(j, i)[2];
            if (v + value >= 256)
                v = 255;
            else if(v + value < 0){
                v = 0;
            }
            else
                v = v + value;
            locMat.at<Vec3b>(j, i)[2] = v;

        }
    }

    //cvtColor(locMat,locMat,COLOR_HSV2BGR);
    cvtColor(locMat,locMat,COLOR_HSV2RGB);

    //밝기(brightness) //알파 문제 있을 수 있음 // HSV에서 조절
    //locMat = locMat + ((brightness_progress-50)*(255.0/50));
    //명암(contrast) //알파 문제 있을 수 있음
    //locMat = locMat * ((contrast_progress-50)*(255.0/50));
    locMat = locMat * progressToValue(contrast_progress,0.25,4,1);

    img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;
}extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageFilterActivity_applyRGBMinGray(JNIEnv *env, jobject thiz,
                                                                         jlong img_input_address,
                                                                         jlong img_output_address) {
    Mat &imgInput = *(Mat *) img_input_address;
    Mat &img_output = *(Mat *) img_output_address;

    Mat locMat =  imgInput.clone();
    //RGB2MinGray(imgInput,locMat);


    int width = imgInput.cols;
    int height = imgInput.rows;
    for (int i = 0; i < imgInput.rows; i++){
        for (int k = 0; k < imgInput.cols; k++){

            unsigned char grayScale = smallestColor(imgInput.at<Vec3b>(i, k)[0],imgInput.at<Vec3b>(i, k)[1],imgInput.at<Vec3b>(i, k)[2]);

            locMat.at<Vec3b>(i, k)[0] = grayScale;
            locMat.at<Vec3b>(i, k)[1] = grayScale;
            locMat.at<Vec3b>(i, k)[2] = grayScale;
        }
    }


    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;

}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageFilterActivity_applyFilterSharp(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jlong img_input_address,
                                                                             jlong img_output_address) {
    Mat &imgInput = *(Mat *) img_input_address;
    Mat &img_output = *(Mat *) img_output_address;

    Mat locMat =  imgInput.clone();

    Mat sharpening_kernel = (Mat_<double>(3, 3) << -1, -1, -1,
            -1, 9, -1,
            -1, -1, -1);
    filter2D(imgInput, locMat, -1, sharpening_kernel);

    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;

}





extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageDenoiseActivity_denoiseColorImage(JNIEnv *env,
                                                                            jobject thiz,
                                                                            jlong input_image_address,
                                                                            jlong output_image_address,
                                                                            jint luminance_progress,
                                                                            jint color_progress) {
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;

    Mat locMat = Mat();

    fastNlMeansDenoisingColored(imgInput,locMat,luminance_progress,color_progress,7,21);

    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageDenoiseActivity_denoiseColorImageMedianFiltering(
        JNIEnv *env, jobject thiz, jlong input_image_address, jlong output_image_address,
        jint luminance_progress, jint color_progress) {
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;

    Mat locMat = Mat();
    int ksize = luminance_progress/2;
    if(ksize%2 != 1)
        ksize += 1;
    medianBlur(imgInput,locMat,ksize);


    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageDenoiseActivity_denoiseColorImageGaussian(JNIEnv *env,
                                                                                       jobject thiz,
                                                                                       jlong input_image_address,
                                                                                       jlong output_image_address,
                                                                                       jint luminance_progress,
                                                                                       jint color_progress) {
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;

    Mat locMat = Mat();

    int ksize = luminance_progress/2;
    if(ksize%2 != 1)
        ksize += 1;
    GaussianBlur(imgInput,locMat,Size(ksize,ksize),1.5);

    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험
    img_output = locMat;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_ImageUtil_maxSize2048(JNIEnv *env, jclass clazz, jlong input_image_address,
                                              jlong output_image_address) {
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;



    ResizeTo2048(imgInput,img_output);
}extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_ImageUtil_maxSizeCustom(JNIEnv *env, jclass clazz,
                                                jlong input_image_address,
                                                jlong output_image_address, jint max_pixel_size) {
    Mat &imgInput = *(Mat *) input_image_address;
    Mat &img_output = *(Mat *) output_image_address;

    ResizeToCustom(imgInput,img_output,max_pixel_size);
}





extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageRemoveShadowActivity_getShadowRemovedImage(JNIEnv *env,
                                                                                        jobject thiz,
                                                                                        jlong input_add,
                                                                                        jlong output_add) {

    Mat &imgInput = *(Mat *) input_add;
    Mat &img_output = *(Mat *) output_add;

    if(img_output.data != nullptr)
        img_output.release();//TODO input이랑 output이 같을경우 위험

    img_output = imgInput.clone();

    Mat img_YCbCr = RGB2YCbCr(imgInput);
    //vector<int> compression_params; //vector that stores the compression parameters of the image
    Mat chan[3];
    split(img_YCbCr, chan);

    Mat G_ = water_filling(chan[2]);
    G_ = incre_filling(G_, chan[2]);

    vector<Mat> channels_(3);
    channels_[0] = chan[0];
    channels_[1] = chan[1];
    channels_[2] = G_;
    Mat img_YCbCr_rec;
    merge(channels_, img_YCbCr_rec);
    Mat rec_img = YCbCr2RGB(img_YCbCr_rec);

    img_output = rec_img;

    img_YCbCr.release();
    chan->release();
    G_.release();

}

extern "C"
JNIEXPORT void JNICALL
Java_com_smu_songgulsongul_activity_EditImageRemoveShadowActivity_lerpShadowRemovedImage(
        JNIEnv *env, jobject thiz, jlong input_add_origin, jlong input_add_shadow_removed,
        jlong output_add, jint progress) {


    Mat &imgInput = *(Mat *) input_add_origin;
    Mat &imgInput2 = *(Mat *) input_add_shadow_removed;
    Mat &img_output = *(Mat *) output_add;


    img_output = imgInput.clone();

    float f_alpha = progress/100.0;
    float f_beta = 1.0-f_alpha;

    addWeighted(imgInput,f_beta,imgInput2,f_alpha,0,img_output);

}