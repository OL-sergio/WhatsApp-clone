package udemy.java.whatsapp_clone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.databinding.ActivityCreateGroupBinding;
import udemy.java.whatsapp_clone.model.User;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;

    private TextView textViewTotal;

    private List<User> listSeletedMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarMain);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textViewTotal = binding.textTotal;

        binding.fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab_createGroup)
                        .setAction("Action", null).show();
            }
        });

        if (getIntent().getExtras() != null){
            List<User> newGroupMembers = (List<User>) getIntent().getExtras().getSerializable("members");
            listSeletedMembers.addAll(newGroupMembers);

            textViewTotal.setText("Total:"  + listSeletedMembers.size());

        }

    }
}