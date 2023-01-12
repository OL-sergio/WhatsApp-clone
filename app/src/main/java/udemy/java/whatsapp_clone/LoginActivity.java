package udemy.java.whatsapp_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityLoginBinding;
import udemy.java.whatsapp_clone.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth userAuthentication;

    private User user;

    private EditText enterEmail, enterPassword;
    private Button loginAccount;

    private TextView goCreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        goCreateUser =  binding.textViewGoCreateAccount;

        enterEmail = binding.textInputEditTextLoginEmail;
        enterPassword = binding.textInputEditTextLoginPassword;

        loginAccount = binding.buttonLogin;

        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = enterEmail.getText().toString();
                String textPassword = enterPassword.getText().toString();

                user = new User();
                user.setEmail(textEmail);
                user.setPassword(textPassword);

                loginToAccount();

            }
        });

        goCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginToAccount() {
        userAuthentication = FirebaseConfiguration.getUserAuthentication();
        userAuthentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}