package com.sally.kotlinwithopencv

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import org.opencv.android.OpenCVLoader
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.BaseLoaderCallback
import org.opencv.core.Mat
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.CvType


class MainActivity : AppCompatActivity() {

    var isGray = false
    var cvCameraView: CameraBridgeViewBase? = null
    var mRgba: Mat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        // sample_text.text = stringFromJNI()

        var btnGray = findViewById<Button>(R.id.btn_convertToGray) as Button
        btnGray.setOnClickListener { isGray = !isGray }

        cvCameraView = findViewById<CameraBridgeViewBase>(R.id.surface_view)
        cvCameraView?.visibility = CameraBridgeViewBase.VISIBLE
        cvCameraView?.setCvCameraViewListener(object : CameraBridgeViewBase.CvCameraViewListener2 {
            override fun onCameraViewStarted(width: Int, height: Int) {
                mRgba = Mat(height, width, CvType.CV_8UC4)
            }

            override fun onCameraViewStopped() {
                mRgba?.release()
            }

            override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
                if (isGray) {
                    val source = inputFrame.gray().nativeObjAddr
                    val destination = mRgba?.getNativeObjAddr()

                    stringFromJNI(source, destination!!)
                } else {
                    mRgba = inputFrame.rgba()
                }
                return mRgba as Mat
            }
        })
    }

    override fun onResume() {
        super.onResume()
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback)
    }

    override fun onPause() {
        super.onPause()

        cvCameraView?.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()

        cvCameraView?.disableView()
    }

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    cvCameraView?.enableView();
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(source: Long, destination: Long)

    companion object {
        val TAG = "OpenCV_Kotlin"

        // Used to load the 'native-lib' library on application startup.
        init {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "OpenCV not loaded")
            } else {
                System.loadLibrary("opencv_java3")
                System.loadLibrary("native-lib")
                Log.d(TAG, "OpenCV loaded");
            }
        }
    }
}
