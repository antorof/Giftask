package es.trigit.gitftask.Pantallas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Vistas.CameraPreview;

public class ActivityCamera extends ActionBarActivity implements SurfaceHolder.Callback {


    private Camera mCamera;
    private Parameters parameters;
    private CameraPreview maPreview;
    private SurfaceHolder sHolder;

    @InjectView(R.id.camera_preview)
    FrameLayout maLayoutPreview;
    @InjectView(R.id.llActivityCamera_top)
    LinearLayout mPanelTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_camera);
        ButterKnife.inject(this);

        mCamera = Camera.open();
        maPreview = new CameraPreview(this, mCamera);
        maLayoutPreview.addView(maPreview);

        sHolder = maPreview.getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @OnClick(R.id.btActivityCamera_boton)
    public void pulsarBotonFoto(){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.takePicture(null, null, mCall);
            }
        });
        //mCamera.takePicture(null, null, mCall);
    }

    @OnClick(R.id.camera_preview)
    public void pulsarScreen(){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                //camera.takePicture(null, rawCallback, jpegCallback);
            }
        });
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
            Toast.makeText(ActivityCamera.this, "Imagen hecha: "+data.length, Toast.LENGTH_SHORT).show();
            Bitmap bmp= BitmapFactory.decodeByteArray(data, 0, data.length);
            int witdhImg = bmp.getHeight();
            int heigthImg = bmp.getWidth();

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int heightWindow = point.y;
            int heigthTop = mPanelTop.getHeight();
            int puntoCorte = heigthImg*heigthTop/heightWindow;

            Bitmap bmp2 = Bitmap.createBitmap(bmp,puntoCorte,0, witdhImg, witdhImg);

            witdhImg = bmp2.getHeight();
            heigthImg = bmp2.getWidth();








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
