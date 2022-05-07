package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Massegeinput;
import com.example.myapplication.Messageadapter;
import com.example.myapplication.R;
import com.example.myapplication.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolbar,imgSend;

    private Messageadapter messageAdapter;
    private ArrayList<Massegeinput> messages;

    String usernameOfTheRoommate, emailOfRoommate, chatRoomId;

    //max alex => alexmax

    //id of the chat room for max and alex


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massege);

        usernameOfTheRoommate = getIntent().getStringExtra("username_of_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate");

        recyclerView = findViewById(R.id.recycalviewmassege);
        imgSend = findViewById(R.id.imgesend);
        edtMessageInput = findViewById(R.id.editText);
        txtChattingWith = findViewById(R.id.txtchatingwith);
        progressBar= findViewById(R.id.progressmassege);
        imgToolbar= findViewById(R.id.imgtoolbar);

        txtChattingWith.setText(usernameOfTheRoommate);

        messages = new ArrayList<>();

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push().setValue(new Massegeinput(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailOfRoommate,edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
            }
        });
        messageAdapter = new Messageadapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        recyclerView.setAdapter(messageAdapter);


        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgToolbar);

        setUpChatRoom();

    }


    private void setUpChatRoom(){

        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(user.class).getUsername();
                if(usernameOfTheRoommate.compareTo(myUsername)>0){
                    chatRoomId = myUsername + usernameOfTheRoommate;
                }else if(usernameOfTheRoommate.compareTo(myUsername) == 0){
                    chatRoomId = myUsername + usernameOfTheRoommate;
                }else {
                    chatRoomId = usernameOfTheRoommate + myUsername;
                }
                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/"+ chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Massegeinput.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}