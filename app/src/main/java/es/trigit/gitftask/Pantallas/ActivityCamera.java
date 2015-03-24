package es.trigit.gitftask.Pantallas;

import android.hardware.Camera;
import android.hardware.Camera.*;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import es.trigit.gitftask.R;
import es.trigit.gitftask.Vistas.CameraPreview;

public class ActivityCamera extends ActionBarActivity implements SurfaceHolder.Callback {

    FrameLayout maLayoutPreview;
    Camera mCamera;
    private Parameters parameters;
    CameraPreview maPreview;
    private SurfaceHolder sHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_camera);
        mCamera = Camera.open();
        maLayoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

        maPreview = new CameraPreview(this, mCamera);
        maLayoutPreview.addView(maPreview);
        sHolder = maPreview.getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // get camera parameters
        parameters = mCamera.getParameters();
        parameters.setFlashMode("auto");
        parameters.setPreviewSize(1280, 720);

        // set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.


        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();
    }


    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // decode the data obtained by the camera into a Bitmap
            Log.d("ImageTakin", "Done");
            Toast.makeText(ActivityCamera.this, "Imagen hecha: "+data.length, Toast.LENGTH_SHORT).show();
            /**mCamera.stopPreview();
            // release the camera
            mCamera.release();
            Toast.makeText(getApplicationContext(),
                    "Your Picture has been taken !", Toast.LENGTH_LONG)
                    .show();
                */
        }
    };
}
