package udemy.java.whatsapp_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityRegisterBinding;
import udemy.java.whatsapp_clone.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth userAuthentication;

    EditText createName, createEmail, createPassword;
    Button  createUser;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        createName = binding.textInputEditTextRegisterUser;
        createEmail = binding.textInputEditTextRegisterEmail;
        createPassword = binding.textInputEditTextRegisterPassword;

        createUser = binding.buttonRegister;

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textName = createName.getText().toString();
                String textEmail = createEmail.getText().toString();
                String textPassword = createPassword.getText().toString();

                user = new User();
                user.setName(textName);
                user.setEmail(textEmail);
                user.setPassword(textPassword);
                registerUser();

            }
        });

    }

    private void registerUser() {
        userAuthentication = FirebaseConfiguration.getUserAuthentication();
        userAuthentication.createUserWithEmailAndPassword(
                user.getEmail() , user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent( RegisterActivity.this,  LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}