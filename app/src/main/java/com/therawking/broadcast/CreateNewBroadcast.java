package com.therawking.broadcast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateNewBroadcast extends AppCompatActivity {

    EditText chnID, chnDescription, chnName;
    CircleImageView channelDP;
    Uri imageURI;
    String img_URI;
    boolean res;
    Date date;
    ProgressDialog progress_Dialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference stoRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_broadcast);
        setSupportActionBar(findViewById(R.id.MyToolbar));
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        date = new Date();
        auth = FirebaseAuth.getInstance();
        chnID = findViewById(R.id.channelID);
        chnDescription = findViewById(R.id.channelDescription);
        chnName = findViewById(R.id.channelName);
        channelDP = findViewById(R.id.channelDP);
        progress_Dialog = new ProgressDialog(this);

        findViewById(R.id.channelDP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent obj = new android.content.Intent();
                obj.setType("image/*");
                obj.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(obj, "Select Profile Picture"),5);

            }
        });

        findViewById(R.id.createChannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelID = chnID.getText().toString().trim();
                String channelDescription = chnDescription.getText().toString().trim();
                String channelName = chnName.getText().toString().trim();
                database = FirebaseDatabase.getInstance();
                storage = FirebaseStorage.getInstance();
                checkUnique(channelID);
                Snackbar.make(findViewById(R.id.CreateNewBroadcast),"Checking values...",Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(channelID)){
                            chnID.setError("Mandatory field!!");
                        } else if (TextUtils.isEmpty(channelName)) {
                            chnName.setError("Mandatory field!!");
                        } else if (imageURI == null){
                            Toast.makeText(CreateNewBroadcast.this, "Select channel display picture!!", Toast.LENGTH_SHORT).show();
                        } else if (!res) {
                            chnID.setError("This Channel ID is not available!!");
                        } else {
                            progress_Dialog.setCancelable(false);
                            progress_Dialog.setMessage("Creating channel...");
                            progress_Dialog.show();
                            stoRef = storage.getReference().child("ChannelDP").child(channelID);
                            stoRef.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        stoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                img_URI = uri.toString();
                                                Channels obj = new Channels(auth.getUid(),channelID,channelName,channelDescription,img_URI,String.valueOf(date.getTime()),null);
                                                ref = database.getReference().child("Channels").child(channelID);
                                                ref.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            ref = database.getReference().child("Users").child(auth.getUid()).child("channels").child(channelID);
                                                            ChannelModel obj = new ChannelModel(true,channelID,channelName,img_URI);
                                                            ref.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progress_Dialog.dismiss();
                                                                        Toast.makeText(CreateNewBroadcast.this, "Channel created successfully.", Toast.LENGTH_SHORT).show();
                                                                        android.content.Intent intent = new android.content.Intent(CreateNewBroadcast.this, MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        progress_Dialog.dismiss();
                                                                        Toast.makeText(CreateNewBroadcast.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            progress_Dialog.dismiss();
                                                            Toast.makeText(CreateNewBroadcast.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        progress_Dialog.dismiss();
                                        Toast.makeText(CreateNewBroadcast.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                },5000);
            }
        });

    }

    private void checkUnique(String channelID) {
        ref = database.getReference().child("Channels").child(channelID);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    res = false;
                } else {
                    res = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

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
            channelDP.setImageURI(imageURI);
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