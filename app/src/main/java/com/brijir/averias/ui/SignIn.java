package com.brijir.averias.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.brijir.averias.R;
import com.brijir.averias.bd.DatabaseHelper;
import com.brijir.averias.bd.User;
import com.j256.ormlite.dao.Dao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignIn extends AppCompatActivity {

    @BindView(R.id.et_Name)
    EditText etName;

    @BindView(R.id.et_Email)
    EditText etEmail;

    @BindView(R.id.et_Phone)
    EditText etPhone;

    @BindView(R.id.et_Id)
    EditText etId;

    @BindView(R.id.et_SUserName)
    EditText etSUserName;

    @BindView(R.id.et_SPassword)
    EditText etSPassword;

    @BindView(R.id.btn_SSignIn)
    Button btnSSignIn;

    @BindView(R.id.SignInToolbar)
    android.support.v7.widget.Toolbar myToolbar;

    DatabaseHelper helperDB;
    AwesomeValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = myToolbar.findViewById(R.id.ctb_title);
        mTitle.setText("Registro");

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validar();
    }

    @OnClick(R.id.btn_SSignIn)
    public void signIn() {
        if(validation.validate()) {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            int phone = 0;
            try {
                phone = Integer.parseInt(etPhone.getText().toString().trim());
            } catch (NumberFormatException ex) { // handle your exception
                Log.d("SignInID", "Phone error");
            }
            int id = 0;
            try {
                id = Integer.parseInt(etId.getText().toString().trim());
            } catch (NumberFormatException ex) { // handle your exception
                Log.d("SignInID", "Id error");
            }
            String user = etSUserName.getText().toString();
            String pass = etSPassword.getText().toString();

            if (helperDB == null) {
                helperDB = new DatabaseHelper(SignIn.this);
            }

            try {
                Dao<User, Integer> userDao = helperDB.getUserDao();
                List<User> usuarios = helperDB.getUserDao().
                        queryBuilder().where().eq("UserName", user.trim()).query();

                if (usuarios.size() > 0) {
                    Toast.makeText(this, "Este usuario ya se encuentra registrado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User();
                newUser.UserId = id;
                newUser.Name = name;
                newUser.Email = email;
                newUser.Phone = phone;
                newUser.UserName = user;
                newUser.Password = pass;

                userDao.createOrUpdate(newUser);

                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Se presentó un error durante el registro.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validar() {
        validation.addValidation(this, R.id.et_Name, "[a-zA-Z\\s]+", R.string.NameError);
        String PhonePattern = "[0-9]{8}";
        validation.addValidation(this, R.id.et_Email, android.util.Patterns.EMAIL_ADDRESS, R.string.EmailError);
        validation.addValidation(this, R.id.et_Phone, PhonePattern, R.string.PhoneError);
        String IdPattern = "[0-9]{9}";
        validation.addValidation(this, R.id.et_Id, IdPattern, R.string.IdError);
        validation.addValidation(this, R.id.et_SUserName, "[a-z0-9\\s]+", R.string.SUserError);
        String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";
        validation.addValidation(this, R.id.et_SPassword, regexPassword, R.string.SPasswordError);
    }
}
