package com.example.prova_android_n2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity4 extends AppCompatActivity {

    EditText edEmail, edSenha;
    Button btCadastrar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        edEmail = findViewById(R.id.edTextCriaEmail);
        edSenha = findViewById(R.id.edTextCriaSenha);

        btCadastrar = findViewById(R.id.botaoCadastrar);

        mAuth = FirebaseAuth.getInstance();

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edEmail.getText().toString();
                String senha = edSenha.getText().toString();

                // Cria usuário
                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                            usuario.sendEmailVerification();

                            Toast.makeText(MainActivity4.this, "Usuário criado. Verifique seu e-mail.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity4.this, "Usuário NÃO foi criado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
