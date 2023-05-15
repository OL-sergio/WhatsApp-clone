package udemy.java.whatsapp_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.activity.ConfigurationsActivity;
import udemy.java.whatsapp_clone.model.User;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.MyViewHolder> {

    private List<User> usersList;
    private Context context;


    public AdapterContacts (List<User> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName, textViewEmail;
        CircleImageView circleImageViewProfile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.textView_rowViewName);
            this.textViewEmail = itemView.findViewById(R.id.textView_rowViewEmail);
            this.circleImageViewProfile = itemView.findViewById(R.id.circleImage_rowViewUsers);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_users, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User users = usersList.get(position);

        holder.textViewName.setText(users.getName());
        holder.textViewEmail.setText(users.getName());


        if (users.getPhoto() != null){
            Uri uri = Uri.parse(users.getPhoto());
            Glide.with(context)
                    //.asBitmap()
                    .load(uri)
                    .into(holder.circleImageViewProfile);

        }else  {
            holder.circleImageViewProfile.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

}
