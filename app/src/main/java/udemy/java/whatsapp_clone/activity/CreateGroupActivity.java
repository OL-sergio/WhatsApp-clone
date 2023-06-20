package udemy.java.whatsapp_clone.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.adapter.AdapterGroups;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityCreateGroupBinding;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.model.Groups;
import udemy.java.whatsapp_clone.model.User;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;

    private Groups group;
    private List<User> listSeletedMembers = new ArrayList<>();

    private StorageReference storageReference;


    private RecyclerView recyclerViewSelectedUsers;
    private AdapterGroups adapterGroups;

    private TextView textViewTotal;
    private TextView totalSelectedUsers;
    private CircleImageView imageViewCreateGroup;
    private FloatingActionButton fabCreateGroup;
    private EditText editTextGroupName;


    private Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar =  binding.toolbarMain;
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Defina um nome");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //textViewTotal = binding.textTotal;

        totalSelectedUsers = binding.contentCreateGroup.textViewUsersSelectedGroupCount;
        recyclerViewSelectedUsers = binding.contentCreateGroup.recyclerViewGroupSelectedMembersGroup;
        imageViewCreateGroup = binding.contentCreateGroup.circleImageviewCreateImageGroup;
        fabCreateGroup = binding.fabCreateGroup;
        editTextGroupName = binding.contentCreateGroup.editTextTextAddGroupName;

        group = new Groups();

        storageReference = FirebaseConfiguration.getFirebaseStorage();

        imageViewCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                insertFromGalleryActivityResultLauncher.launch(intent);
            }
        });

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupName = editTextGroupName.getText().toString();

                listSeletedMembers.add( FirebaseUsers.getCurrentUserData() );
                group.setMembers(listSeletedMembers);

                group.setName(groupName);
                group.saveGroup();


                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("selectedGroup", group);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null){
            List<User> newGroupMembers = (List<User>) getIntent().getExtras().getSerializable("members");
            listSeletedMembers.addAll(newGroupMembers);

            totalSelectedUsers.setText("Total: "  + listSeletedMembers.size());
           // textViewTotal.setText("Total:"  + listSeletedMembers.size());
        }

        //Adapter configuration
        adapterGroups = new AdapterGroups(listSeletedMembers, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false );
        recyclerViewSelectedUsers.setLayoutManager(layoutManager);
        recyclerViewSelectedUsers.setHasFixedSize(true);
        recyclerViewSelectedUsers.setAdapter(adapterGroups);


    }

    ActivityResultLauncher<Intent> insertFromGalleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result ) {

                    if( result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        Uri url = result.getData().getData();
                        try {

                            image = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

                            if (image != null){

                                Glide.with(CreateGroupActivity.this)
                                        .asBitmap()
                                        .load(image)
                                        .into(imageViewCreateGroup);

                                saveImageOnFirebase();

                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

            private void saveImageOnFirebase() {

                //Retrieve image from firebase
                imageViewCreateGroup.setDrawingCacheEnabled(true);
                imageViewCreateGroup.buildDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                byte [] dataImage = baos.toByteArray();

                //Save image on firebase
                final StorageReference imageRef = storageReference
                        .child("images")
                        .child("groups")
                        //.child(userIdentification)
                        .child(group.getId() + ".jpeg");
                        //.child("group.jpeg");

                UploadTask uploadTask = imageRef.putBytes( dataImage );

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateGroupActivity.this, "Falha á criar imagen ",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(CreateGroupActivity.this, "Sucesso á criar imagen ",
                                Toast.LENGTH_SHORT).show();

                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String url = task.getResult().toString();
                                group.setPhoto(url);

                            }

                        });
                    }
                });
            }
}