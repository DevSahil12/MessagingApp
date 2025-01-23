package com.example.messagingapp.logincredentials;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.messagingapp.R;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // If the user is already authenticated, skip to the appropriate activity
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SplashActivity.this, SplashActivity2.class);
            startActivity(intent);
            finish(); // Prevents returning to this activity
            return; // Exit early to prevent showing the getStarted button
        }

        // Show the Get Started button if the user is not authenticated
        getStarted = findViewById(R.id.btnGetStarted);
        getStarted.setOnClickListener(view -> {
            // Navigate to the SignIn activity
            Intent intent = new Intent(SplashActivity.this, SignIn.class);
            startActivity(intent);
            finish();
        });
    }
}
