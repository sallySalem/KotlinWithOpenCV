package com.sally.kotlinwithopencv

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.BaseLoaderCallback


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OpenCV not loaded 2")
    }

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
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
    external fun stringFromJNI(): String

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
