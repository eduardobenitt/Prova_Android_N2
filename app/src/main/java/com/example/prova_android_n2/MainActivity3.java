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
import com.google.firebase.auth.SignInMethodQueryResult;

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
                Log.d("MainActivity3", "Verificando e-mail no Firebase...");

                // Verificar se a conta com o e-mail informado existe no Firebase
                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            Log.d("MainActivity3", "Result: " + result.getSignInMethods());

                            if (result.getSignInMethods().isEmpty()) {
                                // Nenhuma conta com este e-mail encontrada
                                Toast.makeText(MainActivity3.this, "Nenhuma conta encontrada com este e-mail.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Conta encontrada, enviar e-mail de recuperação de senha
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
                        } else {
                            // Erro ao verificar o e-mail no Firebase
                            Toast.makeText(MainActivity3.this, "Erro ao verificar o e-mail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
