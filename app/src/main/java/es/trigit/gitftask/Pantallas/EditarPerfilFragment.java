package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.trigit.gitftask.R;


public class EditarPerfilFragment extends Fragment {

    @InjectView(R.id.etFragmentEditarPerfil_nombre) MaterialEditText etNombre;
    @InjectView(R.id.etFragmentEditarPerfil_localidad) MaterialEditText etLocalidad;


    public static EditarPerfilFragment newInstance()
    {
        return new EditarPerfilFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        ButterKnife.inject(this, view);
        return view;
    }




    @Override
    public void onDetach() {
        super.onDetach();

    }

}
