package udemy.java.whatsapp_clone.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.adapter.AdapterMessages;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityChatBinding;
import udemy.java.whatsapp_clone.helper.Base64Custom;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.model.Conversations;
import udemy.java.whatsapp_clone.model.Groups;
import udemy.java.whatsapp_clone.model.Message;
import udemy.java.whatsapp_clone.model.User;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private TextView chatUsername;
    private CircleImageView circleImageViewUserPhoto;
    private EditText editTextMessage;
    private ImageView openCamara;
    private ImageButton sendMessages;

    private User userReceiver;
    private User userSender;
    private Groups groupsConversations;

    private String idUserReceiver;
    private String idUserSender;
    private String userIdentification;

    private DatabaseReference databaseReference;
    private DatabaseReference messagesRef;
    private ChildEventListener childEventListenerMessages;
    private StorageReference storageReference;


    private RecyclerView recyclerViewMessages;
    private AdapterMessages adapterMessages;
    private final List<Message> messagesList = new ArrayList<>();

    Bitmap image = null;

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
        openCamara = binding.imageViewOpenCamara;
        sendMessages = binding.imageButtonSendMessage;


        recyclerViewMessages = binding.recyclerViewViewMessages;

        storageReference = FirebaseConfiguration.getFirebaseStorage();
        userIdentification = FirebaseUsers.getUserIdentification();

        //Retrieve user data of current user
        idUserSender = FirebaseUsers.getUserIdentification() ;
        userSender = FirebaseUsers.getCurrentUserData();

        //Retrieving data from intent
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

       if ( bundle.containsKey("selectedGroup") ){

                groupsConversations = (Groups) bundle.getSerializable("selectedGroup");
                idUserReceiver = groupsConversations.getId();

                chatUsername.setText(groupsConversations.getName());

                String photoUrl = groupsConversations.getPhoto();
                if ( photoUrl != null ) {

                    Uri url = Uri.parse(photoUrl);
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewUserPhoto);

                } else {
                    circleImageViewUserPhoto.setImageResource(R.drawable.padrao);
                }

            } else {

                userReceiver = (User) bundle.getSerializable("selectedContact");
                chatUsername.setText(userReceiver.getName());

                String photoUrl = userReceiver.getPhoto();
                if ( photoUrl != null ) {

                    Uri url = Uri.parse(userReceiver.getPhoto());

                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewUserPhoto);
                } else {
                   circleImageViewUserPhoto.setImageResource(R.drawable.padrao);
                }
                // Retrieve data from receiver user
                idUserReceiver = Base64Custom.encryptionBase64( userReceiver.getEmail() );
            }

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

        openCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                takePhotoActivityResultLauncher.launch(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> takePhotoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result ) {
                    if( result.getResultCode() == Activity.RESULT_OK) {


                        assert result.getData() != null;
                        Bundle localImageSelection  = result.getData().getExtras();
                        image  = (Bitmap) localImageSelection.get("data");

                        if (image != null){
                           saveImageOnFirebase();

                        }
                    }
                }
            });

    private void saveImageOnFirebase() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 65, baos);
        byte [] dataImage = baos.toByteArray();

        String imageName = UUID.randomUUID().toString();

        final StorageReference imageRef = storageReference
                .child("images")
                .child(idUserSender)
                .child(imageName);
                //child("profile.jpeg");

        UploadTask uploadTask = imageRef.putBytes( dataImage );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "Falha á enviar imagen ",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ChatActivity.this, "Sucesso á enviar imagen ",
                        Toast.LENGTH_SHORT).show();

                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url = task.getResult().toString();



                        if (userReceiver != null ){

                            Message message = new Message();
                            message.setIdUser(idUserSender);
                            message.setMessage("image.jpeg");
                            message.setImage(url);

                            saveMessage(idUserSender, idUserReceiver, message);

                            saveMessage(idUserReceiver, idUserSender, message);

                        }else {

                            for ( User member : groupsConversations.getMembers() ){

                                String idSenderGroup = Base64Custom.encryptionBase64(member.getEmail());
                                String idUserLoggedGroup = FirebaseUsers.getUserIdentification();

                                Message message = new Message();
                                message.setIdUser( idUserLoggedGroup );
                                message.setMessage( "image.jpg" );
                                message.setUserName( userSender.getName() );
                                message.setImage( url );

                                saveMessage(idSenderGroup, idUserReceiver, message );

                                saveConversations(idSenderGroup, idUserReceiver, userReceiver, message, true);

                            }

                        }

                        Toast.makeText(ChatActivity.this, "Sucesso a realizar enviar da imagem", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }



    private void getUsersMessages() {

        messagesList.clear();

       childEventListenerMessages = messagesRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               Message message = snapshot.getValue(Message.class);
               messagesList.add( message );
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

            if(userReceiver != null ){

                Message message = new Message();
                message.setIdUser( idUserSender );
                message.setMessage(textMessage);


                saveMessage(idUserSender, idUserReceiver, message);

                saveMessage(idUserReceiver, idUserSender, message);

                saveConversations(idUserSender, idUserReceiver, userReceiver, message, false);

                saveConversations(idUserReceiver, idUserSender, userSender, message, false);

            } else {

                for ( User member : groupsConversations.getMembers() ){

                    String idSenderGroup = Base64Custom.encryptionBase64(member.getEmail());
                    String idUserLoggedGroup = FirebaseUsers.getUserIdentification();

                    Message message = new Message();
                    message.setIdUser( idUserLoggedGroup );
                    message.setMessage( textMessage );
                    message.setUserName( userSender.getName() );

                    saveMessage(idSenderGroup, idUserReceiver, message );

                    saveConversations(idSenderGroup, idUserReceiver, userReceiver, message, true);

                }

            }

        }else {
            Toast.makeText(this, "Escreva uma mensagen para enviar", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveConversations(String idSender, String idUserReceiver, User userExhibition, Message message, boolean isGroup  ) {

        Conversations conversationsSender = new Conversations();
        conversationsSender.setIdSender( idSender );
        conversationsSender.setIdReceiver( idUserReceiver );
        conversationsSender.setLastUseMessage( message.getMessage() );

        if (isGroup){
            conversationsSender.setIsGroup("true");
            conversationsSender.setGroup( groupsConversations );

        }else {
            conversationsSender.setUserExhibition( userExhibition );
            conversationsSender.setIsGroup("false");

        }

        conversationsSender.saveConversation();

    }

    private void saveMessage(String idSender , String idUserReceiver, Message message ) {

        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        messagesRef = databaseReference.child("messages");

        messagesRef.child(idSender)
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