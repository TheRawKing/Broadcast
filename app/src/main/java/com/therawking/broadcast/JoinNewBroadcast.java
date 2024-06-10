package com.therawking.broadcast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class JoinNewBroadcast extends AppCompatActivity {

    EditText getChnID;
    ProgressDialog progress_Dialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    boolean res;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_new_broadcast);setSupportActionBar(findViewById(R.id.MyToolbar));
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        auth = FirebaseAuth.getInstance();
        progress_Dialog = new ProgressDialog(this);
        getChnID = findViewById(R.id.getChnID);
        database = FirebaseDatabase.getInstance();

        findViewById(R.id.joinChannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = getChnID.getText().toString().trim();
                checkUnique(ID);
                Snackbar.make(findViewById(R.id.JoinNewBroadcast),"Checking values...",Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(ID)) {
                            getChnID.setError("Mandatory Field!!");
                        } else if (res) {
                            getChnID.setError("Channel does not exists!!");
                        } else {
                            progress_Dialog.setCancelable(false);
                            progress_Dialog.setMessage("Joining channel...");
                            progress_Dialog.show();
                            ref = database.getReference().child("Channels").child(ID).child("channelMembers").child(auth.getUid());
                            ref.setValue(new Date().getTime()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ref = database.getReference().child("Channels").child(ID);
                                        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DataSnapshot data = task.getResult();
                                                    String name = String.valueOf(data.child("channelName").getValue());
                                                    String URI = String.valueOf(data.child("channelDP").getValue());
                                                    ref = database.getReference().child("Users").child(auth.getUid()).child("channels").child(ID);
                                                    ChannelModel obj = new ChannelModel(false,ID,name,URI);
                                                    ref.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progress_Dialog.dismiss();
                                                            Toast.makeText(JoinNewBroadcast.this,"Joined channel Succesfully.",Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(JoinNewBroadcast.this,MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                } else {
                                                    progress_Dialog.dismiss();
                                                    Toast.makeText(JoinNewBroadcast.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        progress_Dialog.dismiss();
                                        Toast.makeText(JoinNewBroadcast.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                },1000);
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
}