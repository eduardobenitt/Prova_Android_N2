package com.example.prova_android_n2;

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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edEmail, edSenha;

    Button btLogar;

    TextView tvUser, tvSenha;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edEmail= findViewById(R.id.editTextEmail);
        edSenha = findViewById(R.id.editTextSenha);

        tvUser = findViewById(R.id.tvCriaUser);
        tvSenha = findViewById(R.id.tvEsqueciSenha);


        btLogar = findViewById(R.id.botaoLogin);


        mAuth = FirebaseAuth.getInstance();


        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String senha = edSenha.getText().toString();
                //login + verifica (e envia) email
                mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verificar usuário logado
                            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                            if (usuario.isEmailVerified()) {
                                // Toast.makeText(MainActivity.this, "Usuário logado.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(MainActivity.this, "Usuário não verificado. Verifique seu e-mail.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao logar.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        tvSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent);

            }
        });


    }
}