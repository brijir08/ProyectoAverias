package com.brijir.averias.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.brijir.averias.R;
import com.brijir.averias.bd.DatabaseHelper;
import com.brijir.averias.bd.User;
import com.brijir.averias.helpers.PreferencesManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_UserName)
    EditText etUserName;

    @BindView(R.id.et_Password)
    EditText etPassword;

    @BindView(R.id.btn_LogIn)
    Button btnLogIn;

    @BindView(R.id.cb_Remember)
    CheckBox cbRemember;

    @BindView(R.id.btn_SignIn)
    Button btnSignIn;

    @BindView(R.id.LogInToolbar)
    android.support.v7.widget.Toolbar myToolbar;

    DatabaseHelper helperDB;
    AwesomeValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = myToolbar.findViewById(R.id.ctb_title);
        mTitle.setText("Proyecto Averías");

        String usuarioStr = PreferencesManager.getUsernameFromPreferences(this);
        etUserName.setText(usuarioStr);

        if(PreferencesManager.getRememberFromPreferences(this)) {
            cbRemember.setChecked(true);
            String passwordStr = PreferencesManager.getPasswordFromPreferences(this);
            etPassword.setText(passwordStr);
            goHome();
        }

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validar();
    }

    @OnClick(R.id.btn_LogIn)
    public void checkLogIn() {
        if(validation.validate()) {
            PreferencesManager.savePreferences(MainActivity.this,
                    etUserName.getText().toString(),
                    etPassword.getText().toString(),
                    cbRemember.isChecked());

            if (helperDB == null) {
                helperDB = new DatabaseHelper(this);
            }

            String userNameIn = etUserName.getText().toString().trim();

            try {
                Dao<User, Integer> userDao = helperDB.getUserDao();
                Where filtro = userDao.queryBuilder()
                        .where()
                        .eq("UserName", userNameIn);

                List<User> users = filtro.query();
                if (users.size() == 0) {
                    Toast.makeText(this, "Este usuario no existe! Favor registrarse.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = users.get(0);
                String passwordIn = etPassword.getText().toString();
                if(!user.Password.equals(passwordIn)){
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                    return;
                }

                goHome();
            } catch (Exception e) {
                Toast.makeText(this, "Se presentó un error durante el inicio de sesión.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"Error al iniciar sesión! Favor verifique los datos.",Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_SignIn)
    public void toSignIn() {
        Intent intentSignIn = new Intent(this, SignIn.class);
        startActivity(intentSignIn);
    }

    private void validar() {
        validation.addValidation(this, R.id.et_UserName, "[a-z0-9\\s]+", R.string.UserError);
        String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";
        validation.addValidation(this, R.id.et_Password, regexPassword, R.string.PasswordError);
    }

    public void goHome() {
        Intent goHome = new Intent(this,Home.class);
        startActivity(goHome);
    }
}
