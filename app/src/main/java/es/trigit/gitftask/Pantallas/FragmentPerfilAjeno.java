package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.trigit.gitftask.R;


public class FragmentPerfilAjeno extends Fragment {

    public static FragmentPerfilAjeno newInstance() {
        FragmentPerfilAjeno fragment = new FragmentPerfilAjeno();
        return fragment;
    }

    public FragmentPerfilAjeno() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.framgent_perfil_ajeno, container, false);
    }



}
