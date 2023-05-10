package udemy.java.whatsapp_clone.activity;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityConfigurationsBinding;
import udemy.java.whatsapp_clone.helper.FirebaseUser;
import udemy.java.whatsapp_clone.helper.Permission;

public class ConfigurationsActivity extends AppCompatActivity {

    private ActivityConfigurationsBinding binding;

    private CircleImageView circleImageViewSetImage;
    private ImageButton imageButtonTakePhoto, imageButtonInsertGallery;
    private EditText userProfileName;
    private StorageReference storageReference;
    private String userIdentification;

    public String[] permissionsNecessary = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfigurationsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbarMain  =  findViewById(R.id.toolbarMain);
        toolbarMain.setTitle("Configurações");
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseConfiguration.getFirebaseStorage();
        userIdentification = FirebaseUser.getUserIdentification();


        circleImageViewSetImage = binding.circleImageViewPhotoProfile;
        imageButtonInsertGallery = binding.imageButtonImageInsertGallery;
        imageButtonTakePhoto = binding.imageButtonImagePhoto;
        userProfileName = binding.editTextTextPersonName;

        //Validation Permissions
        int requestCode = 1;
        Permission.permissionValidation(permissionsNecessary, this, requestCode);

        //Recover data from user
        com.google.firebase.auth.FirebaseUser user = FirebaseUser.getCurrentUser();

        Uri url = user.getPhotoUrl();

        if (url != null ) {

       Glide.with(ConfigurationsActivity.this)
                    .asBitmap()
                    .load(url)
                    .into(circleImageViewSetImage);

        }
        circleImageViewSetImage.setImageResource(R.drawable.padrao);

        userProfileName.setText(user.getDisplayName());

       imageButtonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        imageButtonInsertGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPhoto();
            }
        });

    }

    private void insertPhoto() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        insertFromGalleryActivityResultLauncher.launch(intent);
    }


    private void takePhoto() {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        takePhotoActivityResultLauncher.launch(intent);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
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

                           Glide.with(ConfigurationsActivity.this)
                                   .asBitmap()
                                   .load(image)
                                   .into(circleImageViewSetImage);

                           saveImageOnFirebase();

                       }
                   }
                }
            });

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

                                Glide.with(ConfigurationsActivity.this)
                                        .asBitmap()
                                        .load(image)
                                        .into(circleImageViewSetImage);

                                saveImageOnFirebase();

                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });


    public void saveImageOnFirebase() {

        //Retrieve image from firebase
        circleImageViewSetImage.setDrawingCacheEnabled(true);
        circleImageViewSetImage.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 65, baos);
        byte [] dataImage = baos.toByteArray();

        //Save image on firebase
       final StorageReference imageRef = storageReference
                .child("images")
                .child("profile")
                .child(userIdentification)
                // .child(userIdentification + ".jpeg")
                .child("profile.jpeg");

        UploadTask uploadTask = imageRef.putBytes( dataImage );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfigurationsActivity.this, "Falha á criar imagen ",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ConfigurationsActivity.this, "Sucesso á criar imagen ",
                        Toast.LENGTH_SHORT).show();

                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        updatePhotoUser(url);
                        

                    }
                });
            }
        });
    }

    public void updatePhotoUser(Uri url) {
        FirebaseUser.updateUserPhoto(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults ) {
            if (permissionResult == PackageManager.PERMISSION_DENIED){
                alertValidationPermission();
            }
        }
    }

    private void alertValidationPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para poder utilizar a app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Comfirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}