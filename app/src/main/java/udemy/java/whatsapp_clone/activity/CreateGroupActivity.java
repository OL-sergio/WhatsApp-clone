package udemy.java.whatsapp_clone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.adapter.AdapterGroups;
import udemy.java.whatsapp_clone.databinding.ActivityCreateGroupBinding;
import udemy.java.whatsapp_clone.model.User;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;

    private List<User> listSeletedMembers = new ArrayList<>();

    private RecyclerView recyclerViewSelectedUsers;
    private AdapterGroups adapterGroups;

    private TextView textViewTotal;
    private TextView totalSelectedUsers;

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
}