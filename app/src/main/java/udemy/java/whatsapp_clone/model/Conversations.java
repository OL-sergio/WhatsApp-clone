package udemy.java.whatsapp_clone.model;

import com.google.firebase.database.DatabaseReference;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;

public class Conversations {

    private String idSender;
    private String idReceiver;
    private String lastUseMessage;
    private User userExhibition;
    private String isGroup;
    private Groups group;

    public Conversations() {
        this.setIsGroup("false");
    }

    public void saveConversation(){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference conversionRef = databaseReference.child("conversations");

        conversionRef.child(this.getIdSender())
                .child(this.getIdReceiver())
                .setValue(this);
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getLastUseMessage() {
        return lastUseMessage;
    }

    public void setLastUseMessage(String lastUseMessage) {
        this.lastUseMessage = lastUseMessage;
    }

    public User getUserExhibition() {
        return userExhibition;
    }

    public void setUserExhibition(User userExhibition) {
        this.userExhibition = userExhibition;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
