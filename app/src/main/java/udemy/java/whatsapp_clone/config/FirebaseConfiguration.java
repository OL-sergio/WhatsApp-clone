package udemy.java.whatsapp_clone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfiguration {

    private static FirebaseAuth userAuthentication;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    public static FirebaseAuth getUserAuthentication() {
        if (userAuthentication == null) {
            userAuthentication = FirebaseAuth.getInstance();
        }
        return userAuthentication;
    }


    public static DatabaseReference getDatabaseReference() {
        if ( databaseReference == null) {
            databaseReference= FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static StorageReference getFirebaseStorage(){

        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

}
