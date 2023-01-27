package udemy.java.whatsapp_clone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfiguration {

    private static FirebaseAuth userAuthentication;
    private static DatabaseReference firebaseReference;

    public static FirebaseAuth getUserAuthentication() {
        if (userAuthentication == null) {
            userAuthentication = FirebaseAuth.getInstance();
        }
        return userAuthentication;
    }


    public static DatabaseReference getDatabaseReference() {
        if ( firebaseReference == null) {
            firebaseReference= FirebaseDatabase.getInstance().getReference();
        }
        return firebaseReference;
    }
}
