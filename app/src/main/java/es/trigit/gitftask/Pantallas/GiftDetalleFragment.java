package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 22/03/2015.
 */
public class GiftDetalleFragment extends Fragment{

    public static GiftDetalleFragment newInstance()
    {
        return new GiftDetalleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gift_detalle, container, false);
    }
}
