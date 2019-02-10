package com.dev.ssm.learningfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    String TAG = "RegistrationActivity";

    EditText etName;
    EditText etMail;
    EditText etPassword;
    EditText etConfirmation;

    Button btnCancel;
    Button btnSave;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmation = findViewById(R.id.etPasswordConfirmation);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void registerUser(View view){
        final String name = etName.getText().toString();
        final String mail = etMail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmation = etConfirmation.getText().toString();

        if(password.equals(confirmation)){
            mAuth.createUserWithEmailAndPassword(mail,password)
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();

//                                Updates the display name of the user
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates);
                                finish();
                            }else{
                                Toast.makeText(RegistrationActivity.this,"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Result:"+task.getResult()+"\tException:"+task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mAuth.signOut();
        }
    }
}
