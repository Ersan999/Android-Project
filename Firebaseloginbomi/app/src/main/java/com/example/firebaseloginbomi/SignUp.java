package com.example.firebaseloginbomi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){//email error message
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();

            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ //verifying email is real
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){//password error message
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();

            return;
        }

        if(password.length()<6){ //verify password meets character limit
            editTextPassword.setError("Minimum length password is 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override //if registration is completed
            public void onComplete(@NonNull Task<AuthResult> task) {

             if(task.isSuccessful()){
                 Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
             } else
             {
                 if(task.getException() instanceof FirebaseAuthUserCollisionException){
                     Toast.makeText(getApplicationContext(), "Email is already registered",Toast.LENGTH_SHORT).show();

                 }else
                 {
                     Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                 }
             }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonSignUp:
                registerUser();

                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, MainActivity.class));

                break;
        }
    }
}
