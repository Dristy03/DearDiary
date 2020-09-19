package com.dristy.deardiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShowProfile extends AppCompatActivity {

    private ImageView imageView1,imageView2,backBtn,editBtn;
    private TextView name,emaill,bio,dob;
    private ProgressDialog pd;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageView1=findViewById(R.id.img1);
        imageView2=findViewById(R.id.img2);
        backBtn=findViewById(R.id.backBtnId);
        editBtn=findViewById(R.id.editBtnId);

        name=findViewById(R.id.nameId);
        emaill=findViewById(R.id.emailId);
        bio=findViewById(R.id.bioId);
        dob=findViewById(R.id.dobId);

        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);

        checkForData();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContentMainActivity.class);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
    private void checkForData() {

        pd.show();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();


        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Person person= documentSnapshot.toObject(Person.class);
                        assert person != null;
                        name.setText(person.getName());
                        emaill.setText(person.getEmail());
                        bio.setText(person.getBio());
                        dob.setText(person.getDateOfBirth());
                        storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShowProfile.this)
                                        .load(uri)
                                        .centerCrop()
                                        .into(imageView2);

                                storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(ShowProfile.this).load(uri)
                                                .apply(bitmapTransform(new BlurTransformation(22)))
                                                .into(imageView1);
                                    }
                                });
                                pd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(intent);
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                pd.dismiss();
                Toast.makeText(ShowProfile.this, "Error fetching profile data.Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });

    }
}