package com.dristy.deardiary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WritingActivity extends AppCompatActivity {


    private ImageView backBtn,saveBtn,fontBtn,colorBtn;
    private EditText noteEt;
    private ProgressDialog pd;
    String font="null",color="null";
    int type=0;
    int cnt;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        pd = new ProgressDialog(this,R.style.DialogTheme);
        pd.setTitle("Saving...");
        pd.setCancelable(false);
        backBtn=findViewById(R.id.backBtnId);
        saveBtn=findViewById(R.id.saveTxtBtnId);
        fontBtn=findViewById(R.id.textBtnId);
        colorBtn=findViewById(R.id.colorBtnId);

        noteEt=findViewById(R.id.noteId);

        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(WritingActivity.this);
                View view = inflater.inflate(R.layout.color_alert_dialog, null);

                CardView red=view.findViewById(R.id.redId);
                CardView blue=view.findViewById(R.id.blueId);
                CardView black=view.findViewById(R.id.blackId);
                CardView green=view.findViewById(R.id.greenId);
                CardView purple=view.findViewById(R.id.purpleId);
                CardView pink=view.findViewById(R.id.pinkId);
                final AlertDialog alertDialog = new AlertDialog.Builder(WritingActivity.this)
                        .setView(view)
                        .create();

                red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Red));
                        color="#DA0707";
                    }
                });
                blue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Blue));
                        color="#0766DA";
                    }
                });
                black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Black));
                        color="#000000";
                    }
                });
                green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Green));
                        color="#E45183";
                    }
                });
                pink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Pink));
                        color="#E45183";
                    }
                });
                purple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTextColor(getResources().getColor(R.color.Purple));
                        color="#AE0ECA";
                    }
                });
                alertDialog.show();
            }
        });

        fontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(WritingActivity.this);
                View view = inflater.inflate(R.layout.font_alert_dialog, null);

               TextView acme=view.findViewById(R.id.acmeId);
               TextView causal=view.findViewById(R.id.causalId);
               TextView lan=view.findViewById(R.id.lsId);
               TextView mono=view.findViewById(R.id.monoId);
               TextView sans=view.findViewById(R.id.sansId);
               TextView paci=view.findViewById(R.id.pacId);

                final AlertDialog alertDialog = new AlertDialog.Builder(WritingActivity.this)
                        .setView(view)
                        .create();

                acme.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Typeface face = ResourcesCompat.getFont(WritingActivity.this, R.font.font1);;
                        noteEt.setTypeface(face);
                        font="font1";
                        type=2;
                    }
                });
                causal.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        noteEt.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                        font="casual";
                        type=1;
                    }
                });
                lan.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Typeface face = ResourcesCompat.getFont(WritingActivity.this, R.font.font2);;
                        noteEt.setTypeface(face);
                        font="font2";
                        type=2;
                    }
                });
                mono.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        noteEt.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                        font="monospace";
                        type=1;
                    }
                });
                paci.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Typeface face = ResourcesCompat.getFont(WritingActivity.this, R.font.font3);;
                        noteEt.setTypeface(face);
                        font="font3";
                        type=2;
                    }
                });
                sans.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        noteEt.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                        font="sans-serif-condensed";
                    }
                });


                alertDialog.show();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String len=noteEt.getText().toString().trim();
                if (len.length() < 1) {
                    noteEt.setError("Can't save empty note!");
                    noteEt.setFocusable(true);
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
                        cnt = document.getLong("NoteCounter").intValue();
                        Id = Integer.toString(cnt);

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

        String note=noteEt.getText().toString().trim();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("Note",note);
        map.put("Priority",a);
        map.put("CurrentDate",currentDate);
        map.put("Font",font);
        map.put("Color",color);
        map.put("Type",type);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Notes").document(email).collection("Details").document(Id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        noteEt.setText("");
                        Toast.makeText(WritingActivity.this,"Saved",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(WritingActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}