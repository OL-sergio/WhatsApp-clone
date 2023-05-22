package udemy.java.whatsapp_clone.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.adapter.AdapterMessages;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityChatBinding;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.helper.Base64Custom;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.model.Message;
import udemy.java.whatsapp_clone.model.User;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private TextView chatUsername;
    private CircleImageView circleImageViewUserPhoto;
    private EditText editTextMessage;
    private ImageView selectPhoto;
    private ImageButton sendMessages;

    private User userReceived;

    private String idUserReceiver;
    private String idUserSender;

    private DatabaseReference databaseReference;
    private DatabaseReference messagesRef;
    private ChildEventListener childEventListenerMessages;

    private RecyclerView recyclerViewMessages;
    private AdapterMessages adapterMessages;
    private List<Message> messagesList = new ArrayList<>();

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
        editTextMessage = binding.editTextChatMessaging;
        selectPhoto = binding.imageViewOpenCamara;
        sendMessages = binding.imageButtonSendMessage;

        recyclerViewMessages = binding.recyclerViewViewMessages;

        //Retrieve user data of current user
        idUserSender = FirebaseUsers.getUserIdentification() ;

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

            // Retrieve data from receiver user
            idUserReceiver = Base64Custom.encryptionBase64( userReceived.getEmail() );

         }

        adapterMessages = new AdapterMessages(messagesList, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMessages.setLayoutManager( layoutManager);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setAdapter(adapterMessages);

        databaseReference = FirebaseConfiguration.getDatabaseReference();
        messagesRef = databaseReference.child("messages")
                .child(idUserSender)
                .child(idUserReceiver);

           sendMessages.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   sendMessages();
               }
           });



    }

    private void getUsersMessages() {

       childEventListenerMessages = messagesRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Message message = snapshot.getValue(Message.class);
               messagesList.add(message);
               adapterMessages.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private void sendMessages() {

        String textMessage = editTextMessage.getText().toString();


        if (!textMessage.isEmpty()) {

            Message message = new Message();
            message.setIdUser( idUserSender );
            message.setMessage(textMessage);

            saveMessage(idUserSender, idUserReceiver, message);


        }else {
            Toast.makeText(this, "Escreva mensagen para enviar", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveMessage(String idSender , String idUserReceiver, Message message ) {

        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        messagesRef = databaseReference.child("messages");

        messagesRef.child(idUserSender)
                .child(idUserReceiver)
                .push()
                .setValue(message);

        editTextMessage.setText("");

    }

    @Override
    protected void onStart() {
        getUsersMessages();
        super.onStart();
    }

    @Override
    protected void onStop() {

        messagesRef.removeEventListener(childEventListenerMessages);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}