package udemy.java.whatsapp_clone.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;

public class Group implements Serializable {
    private String id;
    private String name;
    private String photo;
    private List<User> members;

    public Group() {

        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference groupRef = databaseReference.child("groups");

        String idGroupFirebase = groupRef.push().getKey();
        setId(idGroupFirebase);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
