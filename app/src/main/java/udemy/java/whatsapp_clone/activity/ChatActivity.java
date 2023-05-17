package udemy.java.whatsapp_clone.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.databinding.ActivityChatBinding;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.model.User;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private TextView chatUsername;
    private CircleImageView circleImageViewUserPhoto;
    private User userReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbarMain  =  findViewById(R.id.toolbarMain);
        toolbarMain.setTitle("");
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatUsername = binding.textViewChatToolbarUserName;
        circleImageViewUserPhoto = binding.circleImageChatToolbarProfileImage;


        //Retrieving data from intent
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

            userReceived = (User) bundle.getSerializable("selectedContact");
            chatUsername.setText(userReceived.getName());

            String photoUrl = userReceived.getPhoto();
            if ( photoUrl != null ) {

                Uri url = Uri.parse(userReceived.getPhoto());

                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(circleImageViewUserPhoto);
            } else {
                circleImageViewUserPhoto.setImageResource(R.drawable.padrao);
            }
        }
    }
}