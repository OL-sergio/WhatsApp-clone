package udemy.java.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import udemy.java.whatsapp_clone.R;
import udemy.java.whatsapp_clone.config.FirebaseConfiguration;
import udemy.java.whatsapp_clone.databinding.ActivityRegisterBinding;
import udemy.java.whatsapp_clone.helper.Base64Custom;
import udemy.java.whatsapp_clone.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth userAuthentication;

    private User user;

    private TextInputEditText createName;
    private TextInputEditText createEmail;
    private TextInputEditText createPassword;
    private Button  createUser;

    private String textName, textEmail, textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        ConstraintLayout view = binding.getRoot();
        setContentView(view);

        createName = binding.textInputEditTextRegisterUser;
        createEmail = binding.textInputEditTextRegisterEmail;
        createPassword = binding.textInputEditTextRegisterPassword;

        createUser = binding.buttonRegister;

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textName = createName.getText().toString();
                textEmail = createEmail.getText().toString();
                textPassword = createPassword.getText().toString();

                createUser();
            }
        });
    }

    private void createUser() {

        if (!textName.isEmpty()){
            if (!textEmail.isEmpty()){
                if (!textPassword.isEmpty()){

                    user = new User();
                    user.setName(textName);
                    user.setEmail(textEmail);
                    user.setPassword(textPassword);
                    registerUser();

                }else {
                    Toast.makeText(this, R.string.intreduza_a_sua_password , Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.intreduza_email, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, R.string.Intreduza_nome, Toast.LENGTH_SHORT).show();
        }

    }

    private void registerUser() {

        userAuthentication = FirebaseConfiguration.getUserAuthentication();
        userAuthentication.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, R.string.sucesso_criar_utilizador, Toast.LENGTH_SHORT).show();
                    finish();

                    try {
                        String idUserIdentification = Base64Custom.encryptionBase64(user.getEmail());
                        user.setUID(idUserIdentification);
                        user.saveUser();


                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    //startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    userValidation(task);
                }
            }
        });
    }

    private void userValidation(Task task) {
        String exception;
        try {
            throw  task.getException();
        } catch (FirebaseAuthWeakPasswordException e ) {
            exception = getString(R.string.Intreduza_senha_mais_forte);
        } catch (FirebaseAuthInvalidCredentialsException e ) {
            exception = getString(R.string.Intreduza_email_valido);
        } catch (FirebaseAuthUserCollisionException e ) {
            exception = getString(R.string.Esta_conta_existe);
        } catch (Exception e ){
            exception = getString(R.string.Erro_criar_utilizador) + e.getMessage();
            e.printStackTrace();
        }
        Toast.makeText(RegisterActivity.this, exception, Toast.LENGTH_SHORT).show();
    }
}