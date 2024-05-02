package com.therawking.broadcast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class sign_up_screen extends AppCompatActivity {
    Button to_Login,proceed_Signing_Up;
    EditText user_name, new_Email, set_Pass, re_Pass;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference stoRef;
    CircleImageView profile_Pic;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageURI;
    String img_URI;
    ProgressDialog progress_Dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        to_Login = findViewById(R.id.toLogin);
        proceed_Signing_Up = findViewById(R.id.proceedSigningUp);
        user_name = findViewById(R.id.username);
        new_Email = findViewById(R.id.newEmail);
        set_Pass = findViewById(R.id.setPass);
        re_Pass = findViewById(R.id.rePass);
        profile_Pic = findViewById(R.id.profilePic);
        progress_Dialog = new ProgressDialog(this);

        profile_Pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent obj = new android.content.Intent();
                obj.setType("image/*");
                obj.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(obj, "Select Profile Picture"),5);

            }
        });

        proceed_Signing_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getText().toString().trim();
                String mail = new_Email.getText().toString().trim();
                String password = set_Pass.getText().toString();
                String repassword = re_Pass.getText().toString();

                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
                    Toast.makeText(sign_up_screen.this, "Some fields are still empty!!", Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailPat)) {
                    new_Email.setError("Type a valid e-mail address.");
                }
                else if (password.length()<6) {
                    set_Pass.setError("Password must be longer than 6 characters.");
                } else if (!password.equals(repassword)) {
                    set_Pass.setError("Password doesn't match.");
                    re_Pass.setError("Password doesn't match.");
                }
                else {
                    progress_Dialog.setCancelable(false);
                    progress_Dialog.setMessage("Signing Up...");
                    progress_Dialog.show();
                    auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                database = FirebaseDatabase.getInstance();
                                storage = FirebaseStorage.getInstance();
                                ref = database.getReference().child("Users").child(id);
                                stoRef = storage.getReference().child("Uploads").child(id);
                                if (imageURI != null) {
                                    stoRef.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                stoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        img_URI = uri.toString();
                                                        Users obj = new Users(id,mail,password,name,img_URI);
                                                        ref.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(sign_up_screen.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                                                    progress_Dialog.dismiss();
                                                                    android.content.Intent intent = new android.content.Intent(sign_up_screen.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    progress_Dialog.dismiss();
                                                                    Toast.makeText(sign_up_screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    img_URI = "https://firebasestorage.googleapis.com/v0/b/broadcast-app01.appspot.com/o/default_profile_picture.png?alt=media&token=fc52d0a5-2ed9-4f1b-a77e-d061ba07cb77";
                                    Users obj = new Users(id,mail,password,name,img_URI);
                                    ref.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(sign_up_screen.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                                progress_Dialog.dismiss();
                                                android.content.Intent intent = new android.content.Intent(sign_up_screen.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                progress_Dialog.dismiss();
                                                Toast.makeText(sign_up_screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                progress_Dialog.dismiss();
                                Toast.makeText(sign_up_screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5 && resultCode==RESULT_OK) {
            if (data != null) {
                imageURI = data.getData();
                resizeImage(imageURI);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageURI = result.getUri();
            profile_Pic.setImageURI(imageURI);
        }
    }

    private void resizeImage(Uri imageURI) {
        CropImage.activity(imageURI)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

}