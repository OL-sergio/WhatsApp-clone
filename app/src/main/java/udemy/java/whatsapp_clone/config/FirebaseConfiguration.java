package udemy.java.whatsapp_clone.config;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseConfiguration {

    private static FirebaseAuth userAuthentication;

    public static FirebaseAuth getUserAuthentication() {
        if (userAuthentication == null) {
            userAuthentication = FirebaseAuth.getInstance();
        }
        return userAuthentication;
    }

}
