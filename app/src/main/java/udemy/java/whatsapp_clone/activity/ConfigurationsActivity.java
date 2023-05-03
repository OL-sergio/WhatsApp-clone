package udemy.java.whatsapp_clone.activity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.util.Objects;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.databinding.ActivityConfigurationsBinding;
import udemy.java.whatsapp_clone.helper.Permission;


public class ConfigurationsActivity extends AppCompatActivity {

    private ActivityConfigurationsBinding binding;

    public String[] permissionsNecessary = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    int REQUEST_CAMARA = 100;
    int REQUEST_GALLERY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfigurationsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Validation Permissions
        int requestCode = 1;
        Permission.permissionValidation(permissionsNecessary, this, requestCode);



        Toolbar toolbarMain  =  findViewById(R.id.toolbarMain);
        toolbarMain.setTitle("Configurações");
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.imageButtonImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        binding.imageButtonImageInsertGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPhoto();
            }
        });

    }

    private void insertPhoto() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        someActivityResultLauncher.launch(intent);
    }


    private void takePhoto() {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        someActivityResultLauncher.launch(intent);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result ) {
                   if( result.getResultCode() == Activity.RESULT_OK) {
                       Bitmap image = null;
                       try {
                           switch (result.getResultCode()){

                           }


                       }catch (Exception e) {

                       }
                   }
                }
            });

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