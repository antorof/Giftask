package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.ArrayList;

import es.trigit.gitftask.Conexion.Http;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Button get, post, put, delete;
        ArrayList<NameValuePair> paras;
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        get = (Button) view.findViewById(R.id.getBtn);
        post = (Button) view.findViewById(R.id.postBtn);
        put = (Button) view.findViewById(R.id.putBtn);
        delete = (Button) view.findViewById(R.id.deleteBtn);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition().execute("http://giftask-roommatesteam.rhcloud.com/giftask/rest/tester",Http.GET_STR);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition().execute("http://giftask-roommatesteam.rhcloud.com/giftask/rest/tester",Http.POST_STR);
            }
        });
        return view;
    }

    class AsyncPetition extends AsyncTask< String, Integer, Integer > {
        String url,method,resp;

        protected Integer doInBackground(String... params) {
            url = params[0];
            method = params[1];

            try {
                if (method.equals(Http.GET_STR))
                    resp = Http.responseToString(Http.get(url));
                else if (method.equals(Http.POST_STR))
                    resp = Http.responseToString(Http.post(url));
                Log.i("",resp);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        protected void onPostExecute(Integer result) {
            switch (result){
                case 0:
                    Toast.makeText(getActivity(),resp,Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(),"Error en petición "+method,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),resp,Toast.LENGTH_LONG).show();
            }
        }
    }
}
