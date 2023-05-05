package udemy.java.whatsapp_clone.helper;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;

public class FirebaseUser {

    public  static String getUserIdentification(){

        FirebaseAuth userRef = FirebaseConfiguration.getUserAuthentication();
        String email = Objects.requireNonNull(userRef.getCurrentUser()).getEmail();
        assert email != null;
        String userIdentification = Base64Custom.encryptionBase64(email);

        return  userIdentification;
    }

}
