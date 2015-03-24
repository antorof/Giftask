package es.trigit.gitftask.Pantallas;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.trigit.gitftask.Dialog.DatePickerFragment;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;


public class EditarPerfilFragment extends Fragment implements DatePickerFragment.OnDatePicker{

    @InjectView(R.id.etFragmentEditarPerfil_nickname)
    MaterialEditText etNickname;
    @InjectView(R.id.etFragmentEditarPerfil_nombre)
    MaterialEditText etNombre;
    @InjectView(R.id.etFragmentEditarPerfil_localidad)
    MaterialEditText etLocalidad;
    @InjectView(R.id.etFragmentEditarPerfil_cumpleanos)
    MaterialEditText etCumpleanos;
    @InjectView(R.id.rdFragmentEditarPerfil_hombre)
    RadioButton rbHombre;
    @InjectView(R.id.rdFragmentEditarPerfil_Mujer)
    RadioButton rbMujer;

    public static EditarPerfilFragment newInstance() {
        return new EditarPerfilFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        ButterKnife.inject(this, view);

        etCumpleanos.setFocusable(false);
        etCumpleanos.setClickable(true);
        setHasOptionsMenu(true);


        etCumpleanos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.setTargetFragment(EditarPerfilFragment.this, 0);
                picker.show(getFragmentManager(), "datePicker");

            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        etNombre.setText(Globales.getUsuarioLogueado().getNombre());
        etLocalidad.setText(Globales.getUsuarioLogueado().getLocalidad());
        etCumpleanos.setText(Globales.getUsuarioLogueado().getFechaCumpleanos());
        etNickname.setText(Globales.getUsuarioLogueado().getNickname());
        if(!Globales.getUsuarioLogueado().isMale()){
            rbHombre.setChecked(true);
        }else{
            rbMujer.setChecked(true);
        }
    }

    //--------------------------------------------------------
    //----------------------- MENU ---------------------------
    //--------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editar_perfil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iMenuEditarPerfil_ok:
                saveUsuario();
                break;
        }
        return true;

    }

    public void saveUsuario(){
        Globales.getUsuarioLogueado().setFechaCumpleano(etCumpleanos.getText().toString());
        Globales.getUsuarioLogueado().setLocalidad(etLocalidad.getText().toString());
        Globales.getUsuarioLogueado().setNickname(etNickname.getText().toString());
        Globales.getUsuarioLogueado().setNombre(etNombre.getText().toString());
        if(rbMujer.isChecked())
            Globales.getUsuarioLogueado().setSexo(false);
        else
            Globales.getUsuarioLogueado().setSexo(true);

        Toast.makeText(this.getActivity(),"Perfil de usuario guardado", Toast.LENGTH_LONG).show();
    }

    //--------------------------------------------------------
    //------------------ Interfaz Datepicker -----------------
    //--------------------------------------------------------

    @Override
    public void onDatePicker(String fecha) {
        etCumpleanos.setText(fecha);
    }
}
