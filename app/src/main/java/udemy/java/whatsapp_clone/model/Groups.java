package udemy.java.whatsapp_clone.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.helper.Base64Custom;

public class Groups implements Serializable {
    private String id;
    private String name;
    private String photo;
    private List<User> members;

    public Groups() {

        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference groupRef = databaseReference.child("groups");

        String idGroupFirebase = groupRef.push().getKey();
        setId(idGroupFirebase);

    }

    public void saveGroup() {

        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference groupRef = databaseReference.child("groups");

        groupRef.child(getId()).setValue(this);

        //Save conversations on group
        for ( User members : getMembers() ){

            String idSender = Base64Custom.encryptionBase64(members.getEmail());
            String idReceiver = getId();

            Conversations conversations = new Conversations();
            conversations.setIdSender(idSender);
            conversations.setIdReceiver(idReceiver);
            conversations.setLastUseMessage("");
            conversations.setIsGroup("true");
            conversations.setGroup(this);

            conversations.saveConversation();
        }
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
