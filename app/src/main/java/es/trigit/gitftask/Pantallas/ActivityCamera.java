package es.trigit.gitftask.Pantallas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;
import es.trigit.gitftask.Vistas.CameraPreview;

/**
 * Camara que realiza una foto y la recorta cuadrada. La foto es almacenada en una variable
 * dentro de la clase Globales donde podra ser obtenida posteriormente
 *
 * @author Jorge Gonzalez Peregrin
 */
public class ActivityCamera extends ActionBarActivity implements SurfaceHolder.Callback {

    private final int FLASH_ACTIVADO = 0;
    private final int FLASH_DESACTIVADO = 1;
    private final int FLASH_AUTO = 2;

    private Camera mCamera;
    private Parameters parameters;
    private CameraPreview maPreview;
    private SurfaceHolder sHolder;
    private boolean isBackCamera;
    private int stateFlash;


    @InjectView(R.id.camera_preview)
    FrameLayout maLayoutPreview;
    @InjectView(R.id.llActivityCamera_top)
    LinearLayout mPanelTop;
    @InjectView(R.id.ivCameraPreview_alternar)
    ImageView mIvAlternar;
    @InjectView(R.id.ivCameraPreview_flash)
    ImageView mIvFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_camera);
        ButterKnife.inject(this);

        stateFlash = FLASH_AUTO;

        if (Camera.getNumberOfCameras() == 1)
            mIvAlternar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = elegirCamara();
        setParametersCamera(mCamera);
        maPreview = new CameraPreview(this, mCamera);
        maLayoutPreview.addView(maPreview);

        sHolder = maPreview.getHolder();
        sHolder.addCallback(this);
    }

    //--------------------------------------------------------------------------
    //----------------------------- BOTONES ------------------------------------
    //--------------------------------------------------------------------------

    @OnClick(R.id.btActivityCamera_boton)
    public void pulsarBotonFoto() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.takePicture(null, null, mCall);
            }
        });
    }

    @OnClick(R.id.spaceCameraPreview)
    public void pulsarScreen() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                //camera.takePicture(null, rawCallback, jpegCallback);
            }
        });
    }

    @OnClick(R.id.ivCameraPreview_alternar)
    public void pulsarAlternar() {
        mCamera = elegirCamara();
        setParametersCamera(mCamera);
        displayCamera();
    }

    @OnClick(R.id.ivCameraPreview_flash)
    public void pulsarFlash() {
        parameters = mCamera.getParameters();

        if (stateFlash == FLASH_AUTO) {
            stateFlash = FLASH_DESACTIVADO;
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            mIvFlash.setImageResource(R.mipmap.ic_flash_off_white_24dp);

        } else if (stateFlash == FLASH_DESACTIVADO) {
            stateFlash = FLASH_ACTIVADO;
            parameters.setFlashMode(Parameters.FLASH_MODE_ON);
            mIvFlash.setImageResource(R.mipmap.ic_flash_on_white_24dp);

        } else {
            stateFlash = FLASH_AUTO;
            parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
            mIvFlash.setImageResource(R.mipmap.ic_flash_auto_white_24dp);
        }


        mCamera.setParameters(parameters);
    }

    //--------------------------------------------------------------------------
    //--------------------------- FUNCIONES ------------------------------------
    //--------------------------------------------------------------------------

    private Camera elegirCamara() {
        //Si se estaba ejecutando otra camara la paramos, sino iniciamos con la camara back
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        } else {
            isBackCamera = false;
        }
        Camera camera;

        //Si la camara back esta activa la cambiamos y viceversa
        if (isBackCamera) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        isBackCamera = !isBackCamera;

        return camera;
    }

    //Ajustamos los parametros que tendra nuestra camara
    private void setParametersCamera(Camera camera) {
        //Ponemos el autofocus si esta disponible
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        //Mostramos o no el simbolo de flash si la camara lo permite
        if (parameters.getSupportedFlashModes() == null || parameters.getSupportedFlashModes().isEmpty()) {
            mIvFlash.setVisibility(View.INVISIBLE);
        } else {
            mIvFlash.setVisibility(View.VISIBLE);
            pulsarFlash();
        }
        camera.setParameters(parameters);
    }

    private void displayCamera() {
        try {
            mCamera.setPreviewDisplay(sHolder);
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }


    //--------------------------------------------------------------------------
    //------------------------- SURFACE HOLDER ---------------------------------
    //--------------------------------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        displayCamera();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();

    }

    //--------------------------------------------------------------------------
    //------------------------- OBTENER FOTO ---------------------------------
    //--------------------------------------------------------------------------

    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            int witdhImg = bmp.getHeight();
            int heigthImg = bmp.getWidth();

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int heightWindow = point.y;
            int heigthTop = mPanelTop.getHeight();
            int puntoCorte = heigthImg * heigthTop / heightWindow;

            Matrix matrix = new Matrix();
            if (isBackCamera) {
                matrix.postRotate(90);
            } else {
                matrix.postRotate(-90);
                matrix.postScale(-1, 1);
            }
            Bitmap bmp2 = Bitmap.createBitmap(bmp, puntoCorte, 0, witdhImg, witdhImg, matrix, true);

            Globales.setFotoObtenida(bmp2);
            mCamera.stopPreview();
            mCamera.release();

            setResult(RESULT_OK);
            finish();

        }
    };
}
