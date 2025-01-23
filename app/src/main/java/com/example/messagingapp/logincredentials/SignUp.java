package com.example.messagingapp.logincredentials;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.messagingapp.mainpages.MainActivity;
import com.example.messagingapp.R;
import com.example.messagingapp.modalclass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    TextView loginbut;
    EditText rg_username, rg_email, rg_password, rg_repassword;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing The Account...");
        progressDialog.setCancelable(false);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        loginbut.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        });

        rg_signup.setOnClickListener(v -> {
            String namee = rg_username.getText().toString();
            String emaill = rg_email.getText().toString();
            String password = rg_password.getText().toString();
            String cPassword = rg_repassword.getText().toString();
            String status = "Hey I'm Using This Application";

            if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword)) {
                Toast.makeText(SignUp.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
            } else if (!emaill.matches(emailPattern)) {
                rg_email.setError("Type A Valid Email Here");
            } else if (password.length() < 6) {
                rg_password.setError("Password Must Be 6 Characters Or More");
            } else if (!password.equals(cPassword)) {
                rg_password.setError("The Password Doesn't Match");
            } else {
                // Show progress dialog before starting network operations
                progressDialog.show();

                auth.createUserWithEmailAndPassword(emaill, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        DatabaseReference reference = database.getReference().child("user").child(id);
                        StorageReference storageReference = storage.getReference().child("Upload").child(id);

                        if (imageURI != null) {
                            storageReference.putFile(imageURI).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        imageuri = uri.toString();
                                        Users users = new Users(id, namee, emaill, password, imageuri, status);
                                        reference.setValue(users).addOnCompleteListener(task2 -> {
                                            // Dismiss progress dialog after completion
                                            progressDialog.dismiss();
                                            if (task2.isSuccessful()) {
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    });
                                }
                            });
                        } else {
                            Users users = new Users(id, namee, emaill, password, imageuri, status);
                            reference.setValue(users).addOnCompleteListener(task12 -> {
                                // Dismiss progress dialog after completion
                                progressDialog.dismiss();
                                if (task12.isSuccessful()) {
                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        // Dismiss progress dialog if there was an error
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        rg_profileImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            rg_profileImg.setImageURI(imageURI);
        }
    }
}
