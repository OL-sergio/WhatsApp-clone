package udemy.java.whatsapp_clone.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import udemy.java.whatsapp_clone.databinding.ActivityCreateGroupBinding;


public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


    }

}