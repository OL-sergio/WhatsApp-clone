package udemy.java.whatsapp_clone.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

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
}