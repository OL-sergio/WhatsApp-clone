package udemy.java.whatsapp_clone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.helper.FirebaseUsers;

public class User implements Serializable {

    private String UID;
    private String name;
    private String email;
    private String password;
    private String photo;

    public User() {

    }

    public void saveUser(){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference user = databaseReference
                .child("users")
                .child(getUID());

        user.setValue(this);
    }

    public void updateUser(){
        String userID = FirebaseUsers.getUserIdentification();
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();

        DatabaseReference userRef = databaseReference
                .child("users")
                .child(userID);

        Map<String, Object> userData = mapUpdate();
        userRef.updateChildren(userData);

    }

    @Exclude
    public Map<String, Object> mapUpdate (){
        HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("email", getEmail() );
            userMap.put("name", getName() );
            userMap.put("photo", getPhoto());
        return userMap;
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

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
