package udemy.java.whatsapp_clone.helper;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;

public class FirebaseUser {

    public static String getUserIdentification(){

        FirebaseAuth userRef = FirebaseConfiguration.getUserAuthentication();
        String email = Objects.requireNonNull(userRef.getCurrentUser()).getEmail();
        assert email != null;
        String userIdentification = Base64Custom.encryptionBase64(email);

        return  userIdentification;
    }

    public static com.google.firebase.auth.FirebaseUser getCurrentUser(){
       FirebaseAuth userRef = FirebaseConfiguration.getUserAuthentication();
         return userRef.getCurrentUser();

    }

    public static boolean updateUserPhoto(Uri url){
        try {
            com.google.firebase.auth.FirebaseUser user = getCurrentUser();
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
            com.google.firebase.auth.FirebaseUser user = getCurrentUser();
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
}



