package com.example.prova_android_n2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity3 extends AppCompatActivity {

    EditText edEmail;
    Button btRecupera;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        edEmail = findViewById(R.id.eTextEmail);
        btRecupera = findViewById(R.id.btEnviarEmail);
        mAuth = FirebaseAuth.getInstance();

        btRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();

                // Log de depuração
                Log.d("MainActivity3", "Email informado: " + email);

                // Verificar se o e-mail está vazio
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity3.this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log de depuração
                Log.d("MainActivity3", "Enviando e-mail de recuperação de senha...");

                // Enviar e-mail de recuperação de senha
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity3.this, "E-mail de recuperação de senha enviado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity3.this, "Erro ao enviar e-mail de recuperação de senha.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
