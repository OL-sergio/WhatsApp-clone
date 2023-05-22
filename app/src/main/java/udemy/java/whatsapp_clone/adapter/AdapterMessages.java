package udemy.java.whatsapp_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;


import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.model.Message;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.MyViewHolder> {

    private List<Message> messagesList;
    private Context context;

    private static final int TYPE_RECEIVER  = 0;
    private static final int TYPE_SENDER    = 1;

    public AdapterMessages( List<Message> messageList, Context context ) {
        this.messagesList = messageList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messages;
        ImageView images;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messages = itemView.findViewById(R.id.textView_chatMessage);
            images = itemView.findViewById(R.id.imageView_chatImages);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewItem = null;
        if (viewType == TYPE_SENDER){

         viewItem = LayoutInflater.from(
                    parent.getContext())
                    .inflate( R.layout.row_view_chat_messages_sender, parent, false);

        }else if (viewType== TYPE_RECEIVER){
           viewItem = LayoutInflater.from(
                    parent.getContext())
                    .inflate( R.layout.row_view_chat_messages_receiver, parent, false);
        }

        return new MyViewHolder(viewItem);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Message message = messagesList.get(position);
        String msgText = message.getMessage();
        String msgImage = message.getImage();


        if ( msgImage != null ){
            Uri url = Uri.parse(msgImage);
            Glide.with(context).load(url).into(holder.images);
        }else {
            holder.messages.setText(msgText);
        }

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        Message message = messagesList.get(position);

        String idUser = FirebaseUsers.getUserIdentification();

        if ( idUser.equals( message.getIdUser() ) ){
            return TYPE_SENDER;
        }
        return TYPE_RECEIVER;
    }
}
