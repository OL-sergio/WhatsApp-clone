package udemy.java.whatsapp_clone.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.adapter.AdapterContacts;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityGroupBinding;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.model.User;

public class GroupActivity extends AppCompatActivity {


    private ActivityGroupBinding binding;

    private List<User> listMembers = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
    private FirebaseUser currentUser;
    private ValueEventListener valueEventListenerGroupMembers;

    private RecyclerView recyclerViewSelectedUsers, recyclerViewGroupMembers;
    private AdapterContacts adapterContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbarMain  =  findViewById(R.id.toolbarMain);
        toolbarMain.setTitle("");
        setSupportActionBar(toolbarMain);

        FloatingActionButton fab = (FloatingActionButton) binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "a", Snackbar.LENGTH_LONG).setAction("A",null).show();
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerViewGroupMembers = binding.include.recyclerViewGroupMembers;
        recyclerViewSelectedUsers = binding.include.recyclerViewSelectedUsers;

        //Adapter configuration
        adapterContacts = new AdapterContacts(listMembers, getApplicationContext());

        //RecyclerView configuration
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewGroupMembers.setLayoutManager(layoutManager);
        recyclerViewGroupMembers.setHasFixedSize(true);
        recyclerViewGroupMembers.setAdapter(adapterContacts);

        databaseReference = FirebaseConfiguration.getDatabaseReference().child("users");
        currentUser = FirebaseUsers.getCurrentUser();

    }
    private void recoverContacts() {

        valueEventListenerGroupMembers = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listMembers.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){

                    User users = data.getValue(User.class);
                    String currentUserEmail = currentUser.getEmail();

                    if ( !currentUserEmail.equals( users.getEmail() ) ){
                        listMembers.add( users );
                    }



                }
                adapterContacts.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverContacts();
    }


    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerGroupMembers);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}