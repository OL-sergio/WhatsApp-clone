package udemy.java.whatsapp_clone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import udemy.java.whatsapp_clone.activity.ChatActivity;
import udemy.java.whatsapp_clone.adapter.AdapterContacts;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.FragmentContactsBinding;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.helper.RecyclerItemClickListener;
import udemy.java.whatsapp_clone.model.User;

public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;

    private ArrayList<User> listUsers = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
    private FirebaseUser currentUser;
    private ValueEventListener valueEventListenerGetUsers;


    private RecyclerView recyclerViewUsersContacts;
    private AdapterContacts adapterListContacts;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
       binding = FragmentContactsBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewUsersContacts = binding.recyclerViewViewUsers;

        adapterListContacts = new AdapterContacts(listUsers, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());

        recyclerViewUsersContacts.setLayoutManager(layoutManager);
        recyclerViewUsersContacts.setHasFixedSize(true);
        //recyclerViewUsersContacts.addItemDecoration( new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL ));
        recyclerViewUsersContacts.setAdapter(adapterListContacts);

        //Configuration of click events on the recyclerView.
        recyclerViewUsersContacts.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewUsersContacts,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //startActivity(new Intent(getActivity(), ChatActivity.class));

                                User userSelected = listUsers.get(position);

                                Intent intent = new Intent (getActivity(), ChatActivity.class);
                                intent.putExtra("selectedContact", userSelected);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //startActivity(new Intent(getActivity(), ChatActivity.class));
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            }
                        }
                )
        );

       /*
       * User tasks1 = new User();
        tasks1.setName("User 1");
        listUsers.add(tasks1);

        User tasks2 = new User();
        tasks2.setName("User 2");
        listUsers.add(tasks2);*/

    }

    private void getAllUsers() {

        databaseReference = FirebaseConfiguration.getDatabaseReference().child("users");
        currentUser = FirebaseUsers.getCurrentUser();

        valueEventListenerGetUsers = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listUsers.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){

                    User users = data.getValue(User.class);
                    String currentUserEmail = currentUser.getEmail();

                    if ( !currentUserEmail.equals( users.getEmail() ) ){
                        listUsers.add( users );
                    }

                }
                adapterListContacts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        getAllUsers();
    }

    @Override
    public void onStop() {
        super.onStop();
      databaseReference.removeEventListener(valueEventListenerGetUsers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}