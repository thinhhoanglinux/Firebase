package com.cwd.firebase.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cwd.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mapping();
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFirebase();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LaunchActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    // Anh xa cac widget
    private void mapping() {
        edtEmail = (EditText) findViewById(R.id.launch_email);
        edtPassword = (EditText) findViewById(R.id.launch_password);
        btnLogin = (Button) findViewById(R.id.launch_login);
        btnRegister = (Button) findViewById(R.id.launch_register);
    }

    // Ham dang nhap firebase:
    private void loginFirebase() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // Dang nhap thanh cong
                        startActivity(new Intent(LaunchActivity.this,ProfileActivity.class));

                    } else {
                        // Dang nhap that bai
                        Toast.makeText(LaunchActivity.this, "Login Failure !", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
