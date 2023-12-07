package com.example.prova_android_n2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Esta tela faz uma consulta ao Firebase, que retorna uma coleção de Filmes adicionados aos favoritos
//que são representados também em um RecyclerView
//O recyclerAdapter utilizado é o mesmo da tela de consulta na API.

//funciona e é composta de maneira similar à tela de consulta à API (UsuarioLogado),
//contudo com dados vindos do Firebase
public class MainActivity5 extends AppCompatActivity {

    Button btSair, btVoltar;
    TextView tvTitulo;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Movie> filmeArrayListFavoritos = new ArrayList<>();

    MinhaClasseRecyclerAdapter recyclerAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        recyclerView = findViewById(R.id.recyclerViewFavoritos);

        btVoltar = findViewById(R.id.botaoVoltar);
        btSair = findViewById(R.id.botaoSair);

        searchView = findViewById(R.id.searchViewFavoritos);

        tvTitulo = findViewById(R.id.textViewTitulo);

        tvTitulo.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //searchView aberto
        searchView.setIconified(false);
        //retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(MainActivity5.this, "Saindo", Toast.LENGTH_LONG).show();

                startActivity(new Intent(MainActivity5.this, MainActivity.class));
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity5.this, MainActivity2.class));
            }

        });


        popularArray();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerAdapter.filtrar(s);
                //notifica o adapter para alterações na lista
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //igual acima, para digitação sem submit
                recyclerAdapter.filtrar(s);
                //notifica o adapter para alterações na lista
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void popularArray() {
        Query query;

        //usuário logado no momento
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //limpando o array para a consulta
        filmeArrayListFavoritos.clear();

        //o caminho da query no Firebase (todos os filmes)
        query = databaseReference.child(user.getUid()).child("Favoritos");

        //execução da query. Caso haja dados, cai no método onDataChange
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //este método é assíncrono, se não houver validação dos dados,
                //a lista será montada incorretamente pois não aguarda a consulta
                //assim, o if seguinte é necessário:
                if (dataSnapshot != null) {
                    for (DataSnapshot objDataSnapshot1 : dataSnapshot.getChildren()) {
                        Movie m = objDataSnapshot1.getValue(Movie.class);
                        filmeArrayListFavoritos.add(m);
                    }
                    //setRecyclerView() para montagem e configuração da RecyclerView mas
                    //neste caso, setRecyclerView() tem que ser chamado aqui (dentro e ao final de onDataChange),
                    //de forma que é executado somente após os dados acima serem baixados do Firebase
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setRecyclerView() {

        recyclerAdapter = new MinhaClasseRecyclerAdapter(filmeArrayListFavoritos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}