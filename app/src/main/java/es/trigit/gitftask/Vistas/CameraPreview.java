package es.trigit.gitftask.Vistas;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Georgevik on 24/03/15.
 */
public class CameraPreview extends SurfaceView {

    private static final String TAG = "CameraPreview";

    private Context context;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.context = context;
        mCamera = camera;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Size size1 = getMejorSize();
        setMeasuredDimension(size1.height, size1.width);
    }

    private Size getMejorSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        List<Camera.Size> supportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        List<Camera.Size> posiblesSizes = new ArrayList<>();
        Point point = new Point();
        display.getSize(point);
        int witdhWindow = point.x;
        int heightWindow = point.y;


        //Cogemos los que tengan un size como el de la pantalla
        for (Camera.Size size : supportedPreviewSizes) {
            if (size.height == witdhWindow) {
                posiblesSizes.add(size);
            }
        }

        //Si no encontramos ningun size obtenemos los sizes mayores que el ancho de pantalla
        if (posiblesSizes.isEmpty()) {
            for (Camera.Size size : supportedPreviewSizes) {
                if (size.height > witdhWindow) {
                    posiblesSizes.add(size);
                }
            }
        }

        //Si no hay ningun size mayor al ancho de pantalla, devolvemos el primer size que se soporta
        if (posiblesSizes.isEmpty())
            return supportedPreviewSizes.get(0);

        //Obtenemos el que mejor ratio tenga de entre los posibles
        double mejorRatioDiferencia = Double.MAX_VALUE;
        Camera.Size mejorSize = posiblesSizes.get(0);
        double diferenciaRatio;

        for (Camera.Size size : posiblesSizes) {
            double ratioA = size.height * 1.0 / size.width * 1.0;
            double ratioB = witdhWindow * 1.0 / heightWindow * 1.0;
            diferenciaRatio = Math.abs(ratioA - ratioB);
            if (diferenciaRatio < mejorRatioDiferencia) {
                mejorSize = size;
            }
        }

        return mejorSize;

    }
}