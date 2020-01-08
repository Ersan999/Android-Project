package com.example.firebaseloginbomi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textViewSignup).setOnClickListener(this);


    }


        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.textViewSignup:

                    startActivity(new Intent(this, SignUp.class));
                    break;
        }
    }
}
