#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;
extern "C"
JNIEXPORT void

JNICALL
Java_com_sally_kotlinwithopencv_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */,
        jlong s, jlong d) {
    std::string hello = "Hello from C++";
    Mat& mGr = *(Mat*) s;
    Mat & mRgb = *(Mat*) d;

    cvtColor(mGr, mRgb, CV_GRAY2RGBA, 4);
}
