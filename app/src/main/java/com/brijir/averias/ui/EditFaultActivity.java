package com.brijir.averias.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.brijir.averias.R;
import com.brijir.averias.bd.DatabaseHelper;
import com.brijir.averias.bd.Fault;
import com.brijir.averias.bd.Ubicacion;
import com.brijir.averias.bd.Usuario;
import com.brijir.averias.helpers.PreferencesManager;
import com.brijir.averias.services.FaultService;
import com.brijir.averias.services.MyServiceManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFaultActivity extends AppCompatActivity {

    @BindView(R.id.et_NameFaultE)
    EditText etNameFault;

    @BindView(R.id.et_DescripcionFaultE)
    EditText etDescripcionFault;

    @BindView(R.id.et_TipoFaultE)
    EditText etTipoFault;

    @BindView(R.id.et_LatitudFaultE)
    EditText etLatitudFault;

    @BindView(R.id.et_LongitudFaultE)
    EditText etLongitudFault;

    @BindView(R.id.iv_ImageFaultE)
    ImageView ivImageFault;

    @BindView(R.id.btn_EditFault)
    Button btnEditFault;

    @BindView(R.id.btn_DeleteFault)
    Button btnDeleteFault;

    @BindView(R.id.EditFaultToolbar)
    android.support.v7.widget.Toolbar myToolbar;

    DatabaseHelper helperDB;
    AwesomeValidation validation;
    Usuario usuario = new Usuario();
    String idFaultEdit = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_fault);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = myToolbar.findViewById(R.id.ctb_titleH);
        mTitle.setText("Modificar Avería");

        loadFields();

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        idFaultEdit = getIntent().getExtras().getString("IdFaultEdit");
        validar();
    }

    @OnClick(R.id.btn_EditFault)
    public void editFaults() {
        if (validation.validate() && !idFaultEdit.equals("")) {
            String name = etNameFault.getText().toString().trim();
            String description = etDescripcionFault.getText().toString().trim();
            String type = etTipoFault.getText().toString().trim();
            String lat = etLatitudFault.getText().toString().trim();
            String lon = etLongitudFault.getText().toString().trim();
            String img = "https://i.imgur.com/dDktOI6.png";
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Ubicacion ubicacion = new Ubicacion(lat,lon);

            getDataUser();

            Fault fault = new Fault(idFaultEdit, name, description, type, ubicacion, date, usuario, img);
            FaultService faultService = MyServiceManager.getServiceFault();
            faultService.editFault(idFaultEdit, fault).enqueue(new Callback<Fault>() {
                @Override
                public void onResponse(Call<Fault> call, Response<Fault> response) {
                    Fault resultado = response.body();
                    Toast.makeText(EditFaultActivity.this, "Avería " + resultado.getNombre() + " modificada exitosamente.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Fault> call, Throwable t) {
                    Toast.makeText(EditFaultActivity.this, "Error al editar la avería.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(EditFaultActivity.this, "Error al editar la avería.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFields() {
        idFaultEdit = getIntent().getExtras().getString("IdFaultEdit");

        if (!idFaultEdit.equals("")) {
            FaultService faultService = MyServiceManager.getServiceFault();
            faultService.getFault(idFaultEdit).enqueue(new Callback<Fault>() {
                @Override
                public void onResponse(Call<Fault> call, Response<Fault> response) {
                    Fault resultado = response.body();
                    etNameFault.setText(resultado.getNombre());
                    etDescripcionFault.setText(resultado.getDescripcion());
                    etTipoFault.setText(resultado.getTipo());
                    etLatitudFault.setText(resultado.getUbicacion().getLat());
                    etLongitudFault.setText(resultado.getUbicacion().getLon());
                    Picasso.with(EditFaultActivity.this).load(resultado.getImagen()).into(ivImageFault);
                }

                @Override
                public void onFailure(Call<Fault> call, Throwable t) {
                    Toast.makeText(EditFaultActivity.this, "Error al obtener la informacin de la avería.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_DeleteFault)
    public void deleteFaults() {
        if (!idFaultEdit.equals("")) {
            FaultService faultService = MyServiceManager.getServiceFault();
            faultService.deleteFault(idFaultEdit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(EditFaultActivity.this, "Avería eliminada exitosamente.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditFaultActivity.this, "Error al eliminar la avería.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(EditFaultActivity.this, "Error al eliminar la avería.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataUser() {
        if (helperDB == null) {
            helperDB = new DatabaseHelper(this);
        }

        String usuarioStr = PreferencesManager.getUsernameFromPreferences(this);

        try {
            Dao<Usuario, Integer> userDao = helperDB.getUserDao();
            Where filtro = userDao.queryBuilder()
                    .where()
                    .eq("UserName", usuarioStr);

            List<Usuario> usuarios = filtro.query();
            if (usuarios.size() == 0) {
                Toast.makeText(this, "Error al obtener la información del usuario.", Toast.LENGTH_SHORT).show();
                return;
            }

            usuario = usuarios.get(0);
        } catch (Exception e) {
            Toast.makeText(this, "Se presentó un error durante la consulta.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validar() {
        validation.addValidation(this, R.id.et_NameFaultE, ".+", R.string.NameFault);
        validation.addValidation(this, R.id.et_DescripcionFaultE, ".+", R.string.DescriptionFault);
        validation.addValidation(this, R.id.et_TipoFaultE, ".+", R.string.TypeFault);
        String DecimalPattern = ".+";
        validation.addValidation(this, R.id.et_LatitudFaultE, DecimalPattern, R.string.LatitudeFault);
        validation.addValidation(this, R.id.et_LongitudFaultE, DecimalPattern, R.string.LongitudeFault);
    }
}
