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
public class TimelineFragment extends Fragment
{
    public static TimelineFragment newInstance()
    {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }
}
