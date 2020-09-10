package com.dristy.deardiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentMainActivity extends AppCompatActivity {

    private ImageView logoutBtn;
    private CircleImageView ProfileBtn;
    private CardView ImgTextBtn, writeBtn, speechBtn, notesBtn;
    private TextView nameP;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        logoutBtn = findViewById(R.id.logoutbtnId);
        ImgTextBtn = findViewById(R.id.imgtotxtbtnId);
        ProfileBtn = findViewById(R.id.profilebtnId);
        writeBtn = findViewById(R.id.writingtxtbtnId);
        speechBtn = findViewById(R.id.speechxtbtnId);
        notesBtn=findViewById(R.id.notebtnId);
        nameP = findViewById(R.id.nameId);

        checkforData();
        ImgTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowProfile.class);
                startActivity(intent);
            }
        });
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WritingActivity.class);
                startActivity(intent);
            }
        });
        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpeechTextActivity.class);
                startActivity(intent);
            }
        });
        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ContentMainActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkforData() {
        pd.show();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Person person = documentSnapshot.toObject(Person.class);
                        assert person != null;
                        nameP.setText(person.getName());

                        storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ContentMainActivity.this)
                                        .load(uri)
                                        .centerCrop()
                                        .into(ProfileBtn);
                                pd.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ContentMainActivity.this, "Error fetching profile data.Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

