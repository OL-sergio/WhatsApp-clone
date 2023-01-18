package udemy.java.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import udemy.java.whatsapp_clone.R;
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

    private String textEmail, textPassword;



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

                textEmail = enterEmail.getText().toString();
                textPassword = enterPassword.getText().toString();

                userAuthentication();

            }
        });

           goCreateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity( new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
    }


    private void userAuthentication() {
        if (!textEmail.isEmpty()){
            if (!textPassword.isEmpty()){
                user = new User();
                user.setEmail(textEmail);
                user.setPassword(textPassword);

                loginToAccount();

            }else {
                Toast.makeText(LoginActivity.this, R.string.intreduzir_passaword, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, R.string.intreduza_email, Toast.LENGTH_SHORT).show();
                }
            }

    private void loginToAccount() {
        userAuthentication = FirebaseConfiguration.getUserAuthentication();
        userAuthentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    goToApp();
                } else {
                    String exception;
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidCredentialsException e ) {
                        exception = getString(R.string.Introduza_email_valido) ;
                    } catch (FirebaseAuthInvalidUserException e ){
                        exception = getString(R.string.utilizador_eamil_nao_existe);
                    } catch (Exception e ){
                        exception = getString(R.string.erro_realizar_login) + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void goToApp() {
        startActivity( new Intent(LoginActivity.this, MainActivity.class));
    }
}


