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
import udemy.java.whatsapp_clone.model.User;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.MyViewHolder> {

    private List<User> usersGroupList;
    private Context context;


    public AdapterGroups (List<User> usersGroupList, Context context ){
        this.usersGroupList = usersGroupList;
        this.context = context;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGroupName;
        CircleImageView circleImageViewGroupProfile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewGroupName = itemView.findViewById(R.id.textView_rowViewGroupName);
            this.circleImageViewGroupProfile = itemView.findViewById(R.id.circleImage_rowViewGroupUsers);
        }
    }

    @NonNull
    @Override
    public AdapterGroups.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_selected_users, parent, false);

        return new AdapterGroups.MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGroups.MyViewHolder holder, int position) {

        User users = usersGroupList.get(position);

        holder.textViewGroupName.setText(users.getName());

        if ( users.getPhoto() != null ) {

            Uri uri = Uri.parse(users.getPhoto());
            Glide.with(context)
                    .load(uri)
                    .into(holder.circleImageViewGroupProfile);

        } else {
            holder.circleImageViewGroupProfile.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return usersGroupList.size();
    }
}
