package com.therawking.broadcast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.PathSegment;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_screen extends AppCompatActivity {
    Button proceed_Logging_Up, to_SignUp;
    EditText Mail, Pass;
    FirebaseAuth auth;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progress_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        progress_Dialog = new ProgressDialog(this);
        Mail = findViewById(R.id.getMail);
        Pass = findViewById(R.id.getPass);
        proceed_Logging_Up = findViewById(R.id.proceedLoggingUp);
        proceed_Logging_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(Mail.getText());
                String password = String.valueOf(Pass.getText());

                if (TextUtils.isEmpty(email) || !email.matches(emailPat)){
                    Mail.setError("Enter a valid e-mail.");
                } else if (TextUtils.isEmpty(password) || password.length()<6) {
                    Pass.setError("Enter a valid password.");
                } else {
                    progress_Dialog.setCancelable(false);
                    progress_Dialog.setMessage("Logging In...");
                    progress_Dialog.show();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                try {
                                    Toast.makeText(login_screen.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                    progress_Dialog.dismiss();
                                    android.content.Intent intent = new android.content.Intent(login_screen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                catch (Exception e) {
                                    progress_Dialog.dismiss();
                                    Toast.makeText(login_screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                progress_Dialog.dismiss();
                                Toast.makeText(login_screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        to_SignUp = findViewById(R.id.toSignUp);
        to_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent var = new android.content.Intent(login_screen.this, sign_up_screen.class);
                startActivity(var);
                finish();
            }
        });

    }
}