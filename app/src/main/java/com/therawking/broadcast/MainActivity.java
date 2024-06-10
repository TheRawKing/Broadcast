package com.therawking.broadcast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Animation shrinkingFABAnim, expandingFABAnim, openRotAnim, closeRotAnim;
    RecyclerView recyclerView;
    ChannelAdapter channelAdapter;
    boolean clicked = false;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.MyToolbar));
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setElevation(15); }
        shrinkingFABAnim = AnimationUtils.loadAnimation(this, R.anim.shrinking_fab);
        expandingFABAnim = AnimationUtils.loadAnimation(this, R.anim.expanding_fab);
        openRotAnim = AnimationUtils.loadAnimation(this, R.anim.open_rot);
        closeRotAnim = AnimationUtils.loadAnimation(this, R.anim.close_rot);
        auth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new ChannelAdapter.WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.joinBct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(MainActivity.this,JoinNewBroadcast.class);
                startActivity(obj);
            }
        });

        findViewById(R.id.createBct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(MainActivity.this,CreateNewBroadcast.class);
                startActivity(obj);
            }
        });

        findViewById(R.id.newFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked();
            }
        });

        FirebaseRecyclerOptions<ChannelModel> options =
                new FirebaseRecyclerOptions.Builder<ChannelModel>().setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(auth.getUid()).child("channels"), ChannelModel.class)
                        .build();
        channelAdapter = new ChannelAdapter(options);
        recyclerView.setAdapter(channelAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        channelAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        channelAdapter.stopListening();
    }

    void onFabClicked() {
        setClickable(clicked);
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    void setClickable(boolean b) {
        if (!b) {
            findViewById(R.id.joinBct).setClickable(true);
            findViewById(R.id.createBct).setClickable(true);
        } else {
            findViewById(R.id.joinBct).setClickable(false);
            findViewById(R.id.createBct).setClickable(false);
        }
    }

    void setVisibility(boolean b) {
        if (!b) {
            findViewById(R.id.joinBct).setVisibility(View.VISIBLE);
            findViewById(R.id.joinBctTv).setVisibility(View.VISIBLE);
            findViewById(R.id.createBct).setVisibility(View.VISIBLE);
            findViewById(R.id.createBctTv).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.joinBct).setVisibility(View.INVISIBLE);
            findViewById(R.id.joinBctTv).setVisibility(View.INVISIBLE);
            findViewById(R.id.createBct).setVisibility(View.INVISIBLE);
            findViewById(R.id.createBctTv).setVisibility(View.INVISIBLE);
        }
    }

    void setAnimation (boolean b) {
        if (!b) {
            findViewById(R.id.newFAB).startAnimation(openRotAnim);
            findViewById(R.id.joinBctTv).startAnimation(expandingFABAnim);
            findViewById(R.id.joinBct).startAnimation(expandingFABAnim);
            findViewById(R.id.createBctTv).startAnimation(expandingFABAnim);
            findViewById(R.id.createBct).startAnimation(expandingFABAnim);
        } else {
            findViewById(R.id.newFAB).startAnimation(closeRotAnim);
            findViewById(R.id.joinBctTv).startAnimation(shrinkingFABAnim);
            findViewById(R.id.joinBct).startAnimation(shrinkingFABAnim);
            findViewById(R.id.createBctTv).startAnimation(shrinkingFABAnim);
            findViewById(R.id.createBct).startAnimation(shrinkingFABAnim);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toSettings) {
            Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.toShare) {
            android.content.Intent obj = new android.content.Intent();
            obj.setAction(Intent.ACTION_SEND);
            obj.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_message));
            obj.setType("text/plain");
            startActivity(obj);
        } else if (itemId == R.id.toFeedback) {
            Toast.makeText(MainActivity.this, "Feedback Selected", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.toLogOut) {
            Intent var = new Intent(MainActivity.this, MessageWindow.class);
            //auth.signOut();
            startActivity(var);
            finish();
        }
        return true;
    }
}