package es.trigit.gitftask.Vistas;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.*;
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

        Size size1 =mejorSize();
        setMeasuredDimension(size1.height, size1.width);
    }

    private Size mejorSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        double ratioWindow = width*1.0 / height *1.0;

        List<Camera.Size> supportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        List<Camera.Size> arrayAproxAncho = new ArrayList<>();

        for (Size size : supportedPreviewSizes) {
            if (size.height == width) {
                arrayAproxAncho.add(size);
            }
        }

        if (arrayAproxAncho.isEmpty()) {
            int mejorDiferencia = 999;
            int diferencia;

            for (Size size : supportedPreviewSizes) {
                diferencia = width - size.height;

                if (diferencia < mejorDiferencia && diferencia >= 0) {
                    arrayAproxAncho.add(size);
                    mejorDiferencia = diferencia;
                }
            }
        }

        Size mejorSize = arrayAproxAncho.get(0);

        double mejorRatio = mejorSize.height*1.0 / mejorSize.width *1.0;
        double currentRatio;
        double diferenciaRatio;

        for (Size size : arrayAproxAncho) {
            currentRatio = size.height*1.0 / size.width * 1.0;
            diferenciaRatio = ratioWindow - currentRatio;

            if (diferenciaRatio >= 0 && diferenciaRatio < mejorRatio) {
                mejorRatio = currentRatio;
                mejorSize = size;
            }
        }


        return mejorSize;
    }
}