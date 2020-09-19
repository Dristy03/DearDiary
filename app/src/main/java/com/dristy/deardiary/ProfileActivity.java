package com.dristy.deardiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int GET_IMAGE_REQUST = 1001;
    private ImageButton backButton;
    private CircleImageView proPic;
    private ImageView addButton;
    private TextView nameTv,bioTv,dobTv;
    private Button saveButton;
    private RelativeLayout blur;
    ProgressDialog pd,p;
    private Uri filepath=null;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private byte[] encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        backButton=findViewById(R.id.backbtn);
        proPic=findViewById(R.id.profilePic);
        addButton=findViewById(R.id.addbtnId);
        nameTv=findViewById(R.id.nameId);
        bioTv=findViewById(R.id.bioId);
        dobTv=findViewById(R.id.dobId);
        saveButton=findViewById(R.id.savebtnId);
        blur=findViewById(R.id.blurimageId);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        p = new ProgressDialog(this);
        p.setCancelable(false);
        
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkForData();
        
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectImage();
                    //uploadImage();
                    Log.d(TAG, "onClick: upload er porer line"  );
                }catch (Exception e){
                    Log.d(TAG, "onClick: " + e);
                }

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameTv.getText().toString().length()<1)
                {
                    nameTv.setError("Can't be empty");
                }else if(bioTv.getText().toString().length()<1)
                {
                    bioTv.setError("Can't be empty");
                }else if(dobTv.getText().toString().length()<1)
                {
                    dobTv.setError("Can't be empty");
                }else if(encodedImage==null) {
                    Toast.makeText(ProfileActivity.this, "Please add your profile image before saving!", Toast.LENGTH_SHORT).show();
                }
                else {
                    pd.setTitle("Updating Profile...");
                    pd.show();
                    saveProfile();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        String name=nameTv.getText().toString().trim();
        String bio=bioTv.getText().toString().trim();
        String dob=dobTv.getText().toString().trim();

        if(name.length()<1 || bio.length()<1 || dob.length()<1 || encodedImage==null)
        {
            Toast.makeText(ProfileActivity.this, "Please complete your profile first!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(),ShowProfile.class);
            startActivity(intent);
        }

    }

    private void saveProfile() {
        String name=nameTv.getText().toString().trim();
        String bio=bioTv.getText().toString().trim();
        String dob=dobTv.getText().toString().trim();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        map.put("bio",bio);
        map.put("dateOfBirth",dob);

            FirebaseFirestore.getInstance().collection("Users").document(email).set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            uploadImage();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void uploadImage() {

        try {
            StorageReference reference = storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.putBytes(encodedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata() + " "+ taskSnapshot.getUploadSessionUri());
                            pd.hide();
                            Intent intent = new Intent(getApplicationContext(),ShowProfile.class);
                            startActivity(intent);
                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "onPostExecute: exception on Uploading" + e);
        }
    }
    private void setProfilePic(Bitmap bitmap){
        try {
            Glide.with(ProfileActivity.this)
                    .load(bitmap)
                    .centerCrop()
                    .into(proPic);

        }catch (Exception e){
            Log.d(TAG, "setProfilePic: exception " + e);
        }
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),GET_IMAGE_REQUST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_IMAGE_REQUST && resultCode == RESULT_OK && data!=null
                      && data.getData()!=null)
        {
            filepath=data.getData();
            pd.show();
            Log.d(TAG, "onActivityResult: " + filepath);
            BackgroundImageUpload backgroundImageUpload = new BackgroundImageUpload(new Callback() {
                @Override
                public void ok() {
                    setProfilePic(BitmapFactory.decodeByteArray(encodedImage, 0, encodedImage.length));
                    pd.hide();
                }
            });
            backgroundImageUpload.execute(filepath);
            Log.d(TAG, "onActivityResult: " );
        }
    }
    class BackgroundImageUpload extends AsyncTask<Uri,Void, byte[]>{
        Bitmap bitmap;
        Callback callback;
        BackgroundImageUpload(Callback callback){
            this.callback = callback;
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d(TAG, "doInBackground: " );
            try {
                RotateBitmap rotateBitmap = new RotateBitmap();
                bitmap = rotateBitmap.HandleSamplingAndRotationBitmap(ProfileActivity.this, uris[0]);
            }
            catch (Exception e){
                Log.d(TAG, "doInBackground: exception" + e);
            }
            byte[] bytes = getBytesFromBitmap(bitmap, 10);
            Log.d(TAG, "doInBackground: " + bytes.length);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            encodedImage = bytes;


            callback.ok();
            Log.d(TAG, "onPostExecute: " + encodedImage);
        }

        public byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            return stream.toByteArray();
        }
    }

    private void checkForData() {
        p.setTitle("Please wait..");
        p.show();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();


        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Person person= documentSnapshot.toObject(Person.class);
                        assert person != null;
                        nameTv.setText(person.getName());
                        bioTv.setText(person.getBio());
                        dobTv.setText(person.getDateOfBirth());

                        storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).getBytes(500000)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        encodedImage = bytes;
                                        setProfilePic(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    }
                                });



                        p.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                p.dismiss();
                Toast.makeText(ProfileActivity.this, "Error fetching profile data.Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });

    }
}