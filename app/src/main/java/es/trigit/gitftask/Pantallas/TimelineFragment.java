package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 08/03/2015.
 */
public class TimelineFragment extends Fragment
{
    private GridView gridView;
    private CustomGridViewAdapter mAdapter;
    private ArrayList<String> data = new ArrayList<String>();

    public static TimelineFragment newInstance()
    {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        gridView = (GridView) view.findViewById(R.id.timeline_grid);

        data.add("HOLA");
        data.add("HOLA1");
        data.add("HOLA2");
        data.add("HOLA3");
        data.add("HOLA4");
        data.add("HOLA5");
        data.add("HOLA6");
        data.add("HOLA7");
        data.add("HOLA8");
        data.add("HOLA9");

        mAdapter = new CustomGridViewAdapter(getActivity(), data);
        gridView.setAdapter(mAdapter);
    }

    public class CustomGridViewAdapter extends ArrayAdapter<String>
    {
        private final Context context;

        public CustomGridViewAdapter(Context context, ArrayList<String> values)
        {
            super(context, R.layout.grid_item_gift, values);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.grid_item_gift, parent, false);

            TextView nombreUsuario = (TextView) itemView.findViewById(R.id.gift_subheader_name);
            nombreUsuario.setText(data.get(position));

            return itemView;
        }
    }
}
