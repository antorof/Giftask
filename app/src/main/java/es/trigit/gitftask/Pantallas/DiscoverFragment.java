package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 08/03/2015.
 */
public class DiscoverFragment extends Fragment
{
    public static DiscoverFragment newInstance()
    {
        return new DiscoverFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}
