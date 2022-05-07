package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class friends extends AppCompatActivity {
    private ArrayList<user> users;
    private useradapter useradapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    useradapter.onuserclick onuserclick;
    private SwipeRefreshLayout swipeRefreshLayout;
    String myimgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        progressBar= findViewById(R.id.progressbar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recycalview);
        swipeRefreshLayout = findViewById(R.id.swip);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getusers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onuserclick= new useradapter.onuserclick() {
            @Override
            public void onuserclick(int position) {
                startActivity(new Intent(friends.this,MessageActivity.class)
                        .putExtra("username_of_roommate",users.get(position).getUsername())
                        .putExtra("email_of_roommate",users.get(position).getEmail())
                        .putExtra("img_of_roommate",users.get(position).getProfilepicture())
                        .putExtra("my_img",myimgurl)

                );

               // Toast.makeText(friends.this, "ضغطت على"+users.get(position).getUsername(), Toast.LENGTH_SHORT).show();
            }
        };

        getusers();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.idprofile){
        startActivity(new Intent(friends.this, profile.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void getusers(){
        users.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    users.add(dataSnapshot.getValue(user.class));
                }
                useradapter= new useradapter(users, friends.this, onuserclick);
                recyclerView.setLayoutManager(new LinearLayoutManager(friends.this));
                recyclerView.setAdapter(useradapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for(user user:users){
                    if(user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myimgurl=user.getProfilepicture();
                        return;}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}