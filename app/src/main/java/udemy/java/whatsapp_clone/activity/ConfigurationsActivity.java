package udemy.java.whatsapp_clone.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

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