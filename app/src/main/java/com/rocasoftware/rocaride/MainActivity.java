package com.rocasoftware.rocaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    TextView nuevoUsuario,olvidasteContra;
    MaterialButton inicioSesion;
    TextInputEditText emailEditText,passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            Intent intent = new Intent(MainActivity.this,PrincipalActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            nuevoUsuario = findViewById(R.id.nuevoUsuario);
            inicioSesion = findViewById(R.id.inicioSesion);
            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            olvidasteContra = findViewById(R.id.olvidasteContra);

            mAuth = FirebaseAuth.getInstance();

            nuevoUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            inicioSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validate();
                }
            });


            olvidasteContra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void validate() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Correo Invalido");
            return;
        }
        else
        {
            emailEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 8)
        {
            passwordEditText.setError("Se necesitan mas de 8 caracteres");
            return;
        }
        else if(!Pattern.compile("[0-9]").matcher(password).find())
        {
            passwordEditText.setError("Al menos un numero");
            return;
        }
        else
        {
            passwordEditText.setError(null);
        }
        iniciarSesion(email,password);
    }

    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(MainActivity.this,PrincipalActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Credenciales equivocadas, trata de nuevo",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}