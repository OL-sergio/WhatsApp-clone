package udemy.java.whatsapp_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityMainBinding;
import udemy.java.whatsapp_clone.fragment.ContactsFragment;
import udemy.java.whatsapp_clone.fragment.ConversationsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth userAuthentication;
    private SearchView searchView;
    private FragmentPagerItemAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAuthentication = FirebaseConfiguration.getUserAuthentication();

        Toolbar toolbarMain  =  findViewById(R.id.toolbarMain);
        toolbarMain.setTitle("WhatApp");
        setSupportActionBar(toolbarMain);

        //Configuring the fragments on view pager navigation
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.conversas, ConversationsFragment.class)
                .add(R.string.contacts, ContactsFragment.class)
                .create()
        );

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();



        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                ConversationsFragment fragment = (ConversationsFragment) adapter.getPage(0);
                fragment.reloadConversationsMessages();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Log.d("event","onSearchViewClosed");
                ConversationsFragment fragment = (ConversationsFragment) adapter.getPage(0);
                fragment.reloadConversationsMessages();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("event","onQueryTextSubmit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("event", newText);

                switch (viewPager.getCurrentItem()){

                    case 0:
                        ConversationsFragment conversationsFragment = (ConversationsFragment) adapter.getPage(0);
                            if (newText != null && !newText.isEmpty() ) {
                                conversationsFragment.searchConversations( newText.toLowerCase() );
                            }else {
                                conversationsFragment.reloadConversationsMessages();
                            }
                        break;

                    case 1:

                        ContactsFragment contactsFragment = (ContactsFragment) adapter.getPage(1);
                            if (newText != null && !newText.isEmpty() ) {
                                contactsFragment.searchContacts( newText.toLowerCase() );
                            } else {
                                contactsFragment.reloadSearchContacts();
                            }
                        break;

                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                logOutUser();
                finish();
                break;

            case R.id.menuConfig:
                openConfigurations();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void openConfigurations() {
        startActivity( new Intent(MainActivity.this, ConfigurationsActivity.class));
    }

    private void logOutUser() {
        try {
            userAuthentication.signOut();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}