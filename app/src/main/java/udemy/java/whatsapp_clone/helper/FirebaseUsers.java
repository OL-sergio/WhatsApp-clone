package udemy.java.whatsapp_clone.helper;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.model.User;

public class FirebaseUsers {

    public static String getUserIdentification(){

        FirebaseAuth userRef = FirebaseConfiguration.getUserAuthentication();
        String email = Objects.requireNonNull(userRef.getCurrentUser()).getEmail();
        assert email != null;
        String userIdentification = Base64Custom.encryptionBase64(email);

        return  userIdentification;
    }

    public static FirebaseUser getCurrentUser(){
       FirebaseAuth userRef = FirebaseConfiguration.getUserAuthentication();
         return userRef.getCurrentUser();

    }

    public static boolean updateUserPhoto(Uri url){
        try {
            FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("perfil","Erro ao actualizar foto");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUserName(String name){
        try {
            FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Porfil","Erro ao actualizar nome");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static User getCurrentUserData() {
        FirebaseUser firebaseUser = getCurrentUser();
        User userdata = new User();
        userdata.setEmail(firebaseUser.getEmail() );
        userdata.setName(firebaseUser.getDisplayName() );

        if (firebaseUser.getPhotoUrl() == null){
            userdata.setPhoto("");
        } else {
            userdata.setPhoto(firebaseUser.getPhotoUrl().toString() );
        }
        return userdata;
    }
}



