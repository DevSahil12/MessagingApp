package com.example.messagingapp.logincredentials;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messagingapp.mainpages.MainActivity;
import com.example.messagingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity2 extends AppCompatActivity {

    ImageView logo;
    TextView name, own1, own2;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize UI components and animations
        logo = findViewById(R.id.logoimg);
        name = findViewById(R.id.logonameimg);
        own1 = findViewById(R.id.ownone);
        own2 = findViewById(R.id.owntwo);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        own1.setAnimation(bottomAnim);
        own2.setAnimation(bottomAnim);

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Use Handler to introduce delay before transitioning
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    // If the user is logged in, go to MainActivity
                    Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If no user is logged in, go to the SignIn or SplashActivity
                    Intent intent = new Intent(SplashActivity2.this, SplashActivity.class); // or SignIn.class if that makes more sense
                    startActivity(intent);
                    finish();
                }
            }
        }, 4000); // Delay for 4 seconds
    }
}
