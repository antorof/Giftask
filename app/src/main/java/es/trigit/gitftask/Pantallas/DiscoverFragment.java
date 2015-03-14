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
import org.apache.http.message.BasicNameValuePair;

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
        View view = inflater.inflate(R.layout.fragment_discover, container, false);


        Button get, post, put, delete;
        final ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("param1","paramcontent1"));
        params.add(new BasicNameValuePair("param2","paramcontent2"));
        final String BASE_URL = getResources().getString(R.string.BASE_URL);

        get = (Button) view.findViewById(R.id.getBtn);
        post = (Button) view.findViewById(R.id.postBtn);
        put = (Button) view.findViewById(R.id.putBtn);
        delete = (Button) view.findViewById(R.id.deleteBtn);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition(BASE_URL+"/rest/tester",Http.Method.GET,params).execute();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition(BASE_URL+"/rest/tester",Http.Method.POST,params).execute();
            }
        });
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition(BASE_URL+"/rest/tester",Http.Method.PUT,params).execute();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPetition(BASE_URL+"/rest/tester",Http.Method.DELETE,params).execute();
            }
        });
        return view;
    }

    class AsyncPetition extends AsyncTask< String, Integer, Integer > {
        String url,resp;
        Http.Method method;
        ArrayList<NameValuePair> parameters;

        public AsyncPetition(String url, Http.Method method, ArrayList<NameValuePair> parameters) {
            this.url = url;
            this.method = method;
            this.parameters = parameters;
        }

        protected Integer doInBackground(String... params) {
            try {
                switch (method) {
                    case GET:
                        resp = Http.responseToString(Http.get(url,parameters));
                        break;
                    case POST:
                        resp = Http.responseToString(Http.post(url,parameters));
                        break;
                    case PUT:
                        resp = Http.responseToString(Http.put(url,parameters));
                        break;
                    case DELETE:
                        resp = Http.responseToString(Http.delete(url,parameters));
                        break;
                }
            }catch (IOException e) {
                return 1;
            }
            return 0;
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
