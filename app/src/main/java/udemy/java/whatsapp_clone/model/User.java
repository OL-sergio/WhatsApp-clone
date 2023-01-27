package udemy.java.whatsapp_clone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;

public class User {

    private String UID;
    private String name;
    private String Email;
    private String Password;

    public User() {

    }

    public void saveUser(){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference user = databaseReference.child("users").child(getUID());

        user.setValue(this);
    }

    @Exclude
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Exclude
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
