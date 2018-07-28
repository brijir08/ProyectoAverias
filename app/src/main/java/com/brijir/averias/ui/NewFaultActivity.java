package com.brijir.averias.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFaultActivity extends AppCompatActivity {

    @BindView(R.id.et_NameFault)
    EditText etNameFault;

    @BindView(R.id.et_DescripcionFault)
    EditText etDescripcionFault;

    @BindView(R.id.et_TipoFault)
    EditText etTipoFault;

    @BindView(R.id.et_LatitudFault)
    EditText etLatitudFault;

    @BindView(R.id.et_LongitudFault)
    EditText etLongitudFault;

    @BindView(R.id.iv_ImageFault)
    ImageView ivImageFault;

    @BindView(R.id.btn_CreateFault)
    Button btnCreateFault;

    @BindView(R.id.NewFaultToolbar)
    android.support.v7.widget.Toolbar myToolbar;

    DatabaseHelper helperDB;
    AwesomeValidation validation;
    Usuario usuario = new Usuario();

    private static final int PERM_CODE = 110;
    private static final int REQUEST_PHOTO = 111;
    private Uri mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_fault);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = myToolbar.findViewById(R.id.ctb_titleH);
        mTitle.setText("Reportar Avería");

        Double valorLat = getIntent().getDoubleExtra("LatitudeFaultNew",0);
        Double valorLon = getIntent().getDoubleExtra("LongitudeFaultNew",0);
        if(valorLat != 0) {
            etLatitudFault.setText(valorLat.toString());
        }
        if(valorLon != 0) {
            etLongitudFault.setText(valorLon.toString());
        }

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validar();
    }

    @OnClick(R.id.iv_ImageFault)
    public void shotPhoto() {
        checkPermission();
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            continueShotPhoto();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_CODE);
        }
    }

    private void continueShotPhoto() {
        File archivo = createFile();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mUri = FileProvider.getUriForFile(this, "com.averias.fotos", archivo);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(takePictureIntent, REQUEST_PHOTO);
    }

    private File createFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp;

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );

            return image;
        }catch(Exception e){
            Log.d("Error al crear archivo.", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                ivImageFault.setImageBitmap(imageBitmap);
            }catch(Exception e){
                Log.d("Error al cargar.", e.getMessage());
            }
        }
    }

    @OnClick(R.id.btn_CreateFault)
    public void createFaults() {
        if (validation.validate()) {
            String id = Integer.toString((int) Math.floor(Math.random()*(19500-18500)+18500));
            String name = etNameFault.getText().toString().trim();
            String description = etDescripcionFault.getText().toString().trim();
            String type = etTipoFault.getText().toString().trim();
            String lat = etLatitudFault.getText().toString().trim();
            String lon = etLongitudFault.getText().toString().trim();
            String img = "https://i.imgur.com/dDktOI6.png";
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Ubicacion ubicacion = new Ubicacion(lat,lon);

            getDataUser();

            Fault fault = new Fault(id, name, description, type, ubicacion, date, usuario, img);
            FaultService faultService = MyServiceManager.getServiceFault();
            faultService.createFault(fault).enqueue(new Callback<Fault>() {
                @Override
                public void onResponse(Call<Fault> call, Response<Fault> response) {
                    Fault resultado = response.body();
                    Toast.makeText(NewFaultActivity.this, "Avería " + resultado.getNombre() + " reportada exitosamente.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Fault> call, Throwable t) {
                    Toast.makeText(NewFaultActivity.this, "Error al reportar la avería.", Toast.LENGTH_SHORT).show();
                }
            });
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
        validation.addValidation(this, R.id.et_NameFault, ".+", R.string.NameFault);
        validation.addValidation(this, R.id.et_DescripcionFault, ".+", R.string.DescriptionFault);
        validation.addValidation(this, R.id.et_TipoFault, ".+", R.string.TypeFault);
        String DecimalPattern = ".+";
        validation.addValidation(this, R.id.et_LatitudFault, DecimalPattern, R.string.LatitudeFault);
        validation.addValidation(this, R.id.et_LongitudFault, DecimalPattern, R.string.LongitudeFault);
    }
}
