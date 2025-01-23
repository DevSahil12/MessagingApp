package com.example.messagingapp.mainpages;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp.R;
import com.example.messagingapp.adapter.MessageAdapter;
import com.example.messagingapp.modalclass.MessageModalClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {
    String ReceiverImg, ReceiverUid, ReceiverName, SenderUID;
    CircleImageView profile;
    TextView ReceiverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImg;
    public static String receiverImg;
    CardView SendBtn;
    EditText TextMsg;

    String senderRoom, reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<MessageModalClass> messagesArrayList;
    MessageAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ReceiverName = getIntent().getStringExtra("nameeee");
        ReceiverImg = getIntent().getStringExtra("reciverImg");
        ReceiverUid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        SendBtn = findViewById(R.id.sendbtnn);
        TextMsg = findViewById(R.id.textmsg);
        ReceiverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessageAdapter(this, messagesArrayList);
        messageAdpter.setAdapter(messagesAdapter);


        Picasso.get().load(ReceiverImg).into(profile);
        ReceiverNName.setText("" + ReceiverName);

        SenderUID = firebaseAuth.getUid();

        senderRoom = SenderUID + ReceiverUid;
        reciverRoom = ReceiverUid + SenderUID;


        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModalClass messages = dataSnapshot.getValue(MessageModalClass.class);
                    if (messages != null) {
                        messagesArrayList.add(messages);
                    }
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profilepic").getValue() != null) {
                    senderImg = snapshot.child("profilepic").getValue().toString();
                } else {
                    // Set a default image or handle the case when the profile picture is missing
                    senderImg = "default_image_url"; // Replace with a default image URL or placeholder
                }
                receiverImg = ReceiverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here if necessary
            }
        });

        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = TextMsg.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatWindow.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextMsg.setText("");
                Date date = new Date();
                MessageModalClass messagess = new MessageModalClass(message, SenderUID, date.getTime());

                DatabaseReference chatRef = database.getReference().child("chats");
                chatRef.child(senderRoom).child("messages").push()
                        .setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    chatRef.child(reciverRoom).child("messages").push()
                                            .setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        // Handle the error here
                                                        Toast.makeText(ChatWindow.this, "Error sending message", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Handle the error for the sender's room
                                    Toast.makeText(ChatWindow.this, "Error sending message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
