package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edusername, edemail, edpass;
    private Button buttonsub;
    private TextView textinfo;
    private boolean issign= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edusername = findViewById(R.id.idusername);
        edemail = findViewById(R.id.idemail);
        edpass = findViewById(R.id.idpass);
        buttonsub = findViewById(R.id.edsubmit);
        textinfo  = findViewById(R.id.txtlogininfo);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,friends.class));
            finish();
        }

        buttonsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edemail.getText().toString().isEmpty() || edpass.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "تسجيلك مش مزبوط", Toast.LENGTH_SHORT).show();
                    if(issign&& edusername.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "تسجيلك مش مزبوط", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                if(issign){
                    handleSignUp();
                }
                else {
                    handlelogin();
                }
            }
        });

        textinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(issign){
                    issign= false;
                    edusername.setVisibility(View.GONE);
                    buttonsub.setText("تسجيل الدخول");
                    textinfo.setText("ليس لديك حساب؟ انشئ حساب جديد");
                }
                else {
                    issign = true;
                    buttonsub.setText("انشاء حساب");
                    edusername.setVisibility(View.VISIBLE);
                    textinfo.setText("لديك حساب؟ سجل الدخول");
                }
            }
        });

    }

    private void handlelogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edemail.getText().toString(),edpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,friends.class));

                }
                else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edemail.getText().toString(),edpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new user(edusername.getText().toString(), edemail.getText().toString(),""));
                    Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,friends.class));
                }else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}