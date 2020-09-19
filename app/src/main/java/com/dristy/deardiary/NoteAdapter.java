package com.dristy.deardiary;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.MyViewHolder> {
    private static final String TAG ="ADAPTER" ;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Note model) {
        holder.date.setText(model.getCurrentDate());
        holder.message.setText(model.getNote());
        try{
            if(model.getColor()!="null")
            {
                holder.message.setTextColor(Color.parseColor(model.getColor()));
            }
            if(model.getFont()!="null")
            {
                if(model.getType()==1)
                {
                    holder.message.setTypeface(Typeface.create(model.getFont(), Typeface.NORMAL));
                }
                else
                {
                    Log.d(TAG, "onBindViewHolder: " +"fonts/"+model.getFont()+".ttf");

                    holder.message.setTypeface(Typeface.createFromAsset(context.getAssets(),  "fonts/"+model.getFont()+".ttf"));
                }

            }

        }catch (Exception e)
        {
            Log.d(TAG, "onBindViewHolder: " +e);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row_layout,parent,false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.todayDateId);
            message=itemView.findViewById(R.id.messageId);
        }
    }
}
