package com.cwd.firebase.Activity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword;
    private Button btnConfirm;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapping();
        mAuth = FirebaseAuth.getInstance();

        // Lay duong dan cua note goc tren database:
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFirebase();
            }
        });
    }

    // Anh xa cac widget
    private void mapping() {

        edtName = (EditText) findViewById(R.id.reg_name);
        edtEmail = (EditText) findViewById(R.id.reg_email);
        edtPassword = (EditText) findViewById(R.id.reg_password);
        btnConfirm = (Button) findViewById(R.id.reg_confirm);
    }

    // Ham dang ky firebase:
    private void registerFirebase() {

        final String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        // Neu email & password & name trong 2 cai edit khac null:
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // Lấy id của user vừa đăng kí:
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        // Upload dữ liệu lên firebase database:
                        // Trước khi làm cái này nhớ nhúng thư viện:
                        // compile 'com.google.firebase:firebase-database:11.0.2'
                        mDatabase.child("account").child(userID).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Dang ki thanh cong
                                Toast.makeText(RegisterActivity.this, "Register Success !", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Disconnected !", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        // Dang ki that bai
                        Toast.makeText(RegisterActivity.this, "Register Failure !", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
