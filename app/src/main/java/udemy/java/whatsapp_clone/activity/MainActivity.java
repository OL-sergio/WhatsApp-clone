package udemy.java.whatsapp_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
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
    private MaterialSearchView searchView;

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
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.conversas, ConversationsFragment.class)
                .add(R.string.contacts, ContactsFragment.class)
                .create()
        );



        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        searchView = binding.searchToolbar.searchViewChatUsers;

        //Listener for seatView
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
               // Log.d("event","onSearchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
               // Log.d("event","onSearchViewClosed");
                ConversationsFragment fragment = (ConversationsFragment) adapter.getPage(0);
                fragment.reLoadMessages();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("event","onQueryTextSubmit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("event", newText);
                ConversationsFragment fragment = (ConversationsFragment) adapter.getPage(0);

                if (newText != null && !newText.isEmpty() ) {
                    fragment.searChConversations( newText.toLowerCase() );
                }

                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView.setMenuItem(item);

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