package com.example.madsassignment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_passowrd) EditText edt_passowrd;
    @BindView(R.id.edt_username) EditText edt_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void btn_login()
    {
        if(edt_username.getText().toString().equals("nikhilv")&&edt_passowrd.getText().toString().equals("nikhil1991")){

            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        }
        else
        {
            Toast.makeText(this, "Enter valid username and password", Toast.LENGTH_SHORT).show();
        }
    }
}