package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class profile extends AppCompatActivity {
private Button btlogout;
private ImageView imgprofile;
private Uri imgpath;
private Button btnubload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btlogout= findViewById(R.id.logout);
        imgprofile= findViewById(R.id.profile_img);
        btnubload= findViewById(R.id.uploadimage);
        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        btnubload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });

        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoint = new Intent(Intent.ACTION_PICK);
                photoint.setType("image/*");
                startActivityForResult(photoint, 1);
            }
        });
    }

    private void uploadimage() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("تحميل...");
        progress.show();
        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(imgpath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                updateprofilepic(task.getResult().toString());
                            }

                            }
                    });
                    Toast.makeText(profile.this, "تم تحميل الصوره", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(profile.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
                progress.dismiss();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== RESULT_OK && data!=null){
            imgpath=  data.getData();
            getImageOnimageview();
        }
    }

    private void getImageOnimageview() {
        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgprofile.setImageBitmap(bitmap);

    }

    private void updateprofilepic(String url) {
        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getUid()+"/profilepicture").setValue(url);
    }

}