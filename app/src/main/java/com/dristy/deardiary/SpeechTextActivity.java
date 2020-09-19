package com.dristy.deardiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeechTextActivity extends AppCompatActivity {

    private static final String TAG ="SpeechTextAct" ;
    private EditText txvResult;
    private ImageView backBtn,saveBtn;
    private ProgressDialog pd;
    String font="null",color="null";
int cnt;
String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        txvResult = findViewById(R.id.txvResult);
        backBtn=findViewById(R.id.backBtnId);
        saveBtn=findViewById(R.id.saveTxtBtnId);
        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Saving...");
        pd.setCancelable(false);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String len=txvResult.getText().toString().trim();
                if (len.length() < 1) {
                    txvResult.setError("Can't save empty note!");
                    txvResult.setFocusable(true);
                }
                else
                {
                    pd.show();
                    processTheCounter();
                }
            }
        });
    }

    private void processTheCounter() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference dr = FirebaseFirestore.getInstance().collection("Users").document(email);
        dr.update("NoteCounter", FieldValue.increment(1));


        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cnt = document.getLong("NoteCounter").intValue();
                        Log.d(TAG, "DocumentSnapshot data: " + cnt);
                        Id = Integer.toString(cnt);
                        Log.d(TAG, "DocumentSnapshot data: " + Id);
                        addToDatabase();
                    }
                }
            }
        });
    }

    private void addToDatabase() {
        Calendar calendar= Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a= a*100+DATE;

        MONTH++;
        String mmS = ((MONTH<10)?"0"+MONTH:MONTH+"");
        String ddS = ((DATE<10)?"0"+DATE:DATE+"");
        String currentDate = ddS+"."+mmS+"."+YEAR;

        String note=txvResult.getText().toString().trim();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("Note",note);
        map.put("Priority",a);
        map.put("CurrentDate",currentDate);
        map.put("Font",font);
        map.put("Color",color);
        map.put("Type",0);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Notes").document(email).collection("Details").document(Id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        txvResult.setText("");
                        Toast.makeText(SpeechTextActivity.this,"Saved",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(SpeechTextActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txvResult.setText(result.get(0));
                    txvResult.append(result.get(0));
                    txvResult.setSelection(txvResult.getText().length());
                }
                break;
        }
    }
}