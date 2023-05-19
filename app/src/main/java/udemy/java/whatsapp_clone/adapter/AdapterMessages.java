package udemy.java.whatsapp_clone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import udemy.java.whatsapp_clone.model.Message;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.MyViewHolder> {

    private List<Message> messagesList;
    private Context context;

    public AdapterMessages( List<Message> messageList, Context context ) {
        this.messagesList = messageList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
