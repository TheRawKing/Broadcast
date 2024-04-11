package com.therawking.broadcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class sign_up_screen extends AppCompatActivity {
    Button to_Login,proceed_To_Main;
    EditText user_name, new_Email, set_Pass, re_Pass;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        to_Login = findViewById(R.id.toLogin);
        proceed_To_Main = findViewById(R.id.proceed);
        user_name = findViewById(R.id.username);
        new_Email = findViewById(R.id.newEmail);
        set_Pass = findViewById(R.id.setPass);
        re_Pass = findViewById(R.id.rePass);

        proceed_To_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getText().toString();
                String mail = new_Email.getText().toString();
                String password = set_Pass.getText().toString();
                String repassword = re_Pass.getText().toString();

            }
        });

        to_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent var = new android.content.Intent(sign_up_screen.this, login_screen.class);
                startActivity(var);
                finish();
            }
        });


    }
}