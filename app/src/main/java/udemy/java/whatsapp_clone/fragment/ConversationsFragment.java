package udemy.java.whatsapp_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import udemy.java.whatsapp_clone.activity.ChatActivity;
import udemy.java.whatsapp_clone.adapter.AdapterConversations;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.FragmentConversationsBinding;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;
import udemy.java.whatsapp_clone.helper.RecyclerItemClickListener;
import udemy.java.whatsapp_clone.model.Conversations;


public class ConversationsFragment extends Fragment {

    private FragmentConversationsBinding binding;

    private final ArrayList<Conversations> listConversations= new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
    private DatabaseReference conversationsRef;
    private ChildEventListener childEventListenerConversationsUsers;

    private RecyclerView recyclerViewViewConversations;
    private AdapterConversations adapterListConversations;

    public ConversationsFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConversationsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerViewViewConversations = binding.recyclerViewViewConversations;

        adapterListConversations = new AdapterConversations(listConversations, getActivity());


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerViewViewConversations.setLayoutManager(layoutManager);
        recyclerViewViewConversations.setHasFixedSize(true);
        recyclerViewViewConversations.setAdapter(adapterListConversations);

        recyclerViewViewConversations.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewViewConversations,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Conversations conversationsSelected = listConversations.get(position);

                        Intent intent = new Intent (getActivity(), ChatActivity.class);
                        intent.putExtra("selectedContact", conversationsSelected.getUserExhibition());
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        String userIdentification = FirebaseUsers.getUserIdentification();
        conversationsRef = databaseReference.child("conversations")
                .child( userIdentification );


    }

    private void getUsersConversations() {

        childEventListenerConversationsUsers = conversationsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversations conversationsList = snapshot.getValue(Conversations.class);
                listConversations.add(conversationsList);

                adapterListConversations.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        getUsersConversations();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversationsRef.removeEventListener(childEventListenerConversationsUsers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}