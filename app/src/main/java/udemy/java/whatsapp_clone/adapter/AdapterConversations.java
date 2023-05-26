package udemy.java.whatsapp_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.model.Conversations;
import udemy.java.whatsapp_clone.model.User;


public class AdapterConversations extends RecyclerView.Adapter<AdapterConversations.MyViewHolder> {

    private List<Conversations> conversationsList;
    private Context context;

    public AdapterConversations (List<Conversations> conversationsList, Context context){
        this.conversationsList = conversationsList;
        this.context = context;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewLastMessage;
        CircleImageView circleImageViewProfile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.textView_rowViewConversationsName);
            this.textViewLastMessage = itemView.findViewById(R.id.textView_rowViewConversationsLastMessage);
            this.circleImageViewProfile = itemView.findViewById(R.id.circleImage_rowViewConversationsUsers);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_conversations, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversations conversations = conversationsList.get(position);
        holder.textViewLastMessage.setText(conversations.getLastUseMessage());

        User user = conversations.getUserExhibition();
        holder.textViewName.setText(user.getName());

        if ( user.getPhoto() != null ) {

            Uri uri = Uri.parse(user.getPhoto());
            Glide.with(context)
                    .load(uri)
                    .into(holder.circleImageViewProfile);

        } else {
            holder.circleImageViewProfile.setImageResource(R.drawable.padrao);

        }

    }

    @Override
    public int getItemCount() {
        return conversationsList.size();
    }


}
