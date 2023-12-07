package com.example.prova_android_n2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class MinhaClasseRecyclerAdapter extends RecyclerView.Adapter<MinhaClasseRecyclerAdapter.MyViewHolder> {

    ArrayList<Movie> moviesArrayList = new ArrayList<>();
    ArrayList<Movie> moviesArrayListCopia = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public MinhaClasseRecyclerAdapter(ArrayList<Movie> moviesArrayList_) {
        this.moviesArrayList = moviesArrayList_;
        moviesArrayListCopia = new ArrayList<>(moviesArrayList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView release_date;
        ImageView poster_path;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            release_date = itemView.findViewById(R.id.tvReleaseDate);
            poster_path = itemView.findViewById(R.id.imageView);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getContext().toString().contains("MainActivity2")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar filme")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, salvar no Firebase, método "inserirEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Título salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                                inserirEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
            //2 - estou na tela de favoritos, remover item
            else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Remover")
                        .setMessage("Confirma remover dos favoritos?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, remover do Firebase, método "removerEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Título removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                removerEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
        }


    //todo inserção no Firebase - filmes favoritos do usuário
    private void inserirEm(int layoutPosition) {
        //id do usuário logado no momento
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //objeto da lista clicado
        Movie f = moviesArrayList.get(layoutPosition);

        //salvo o objeto no Firebase

        databaseReference.child(user.getUid()).
                child("Favoritos").
                child(f.getTitle()).
                setValue(f);
    }

}

    //remover no Firebase - filmes favoritos do usuário
    public void removerEm(int layoutPosition) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Movie f = moviesArrayList.get(layoutPosition);

        moviesArrayList.clear();

        databaseReference.child(user.getUid()).child("Favoritos").
                child(f.getTitle()).
                removeValue();
    }

    @NonNull
    @Override
    public MinhaClasseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MinhaClasseRecyclerAdapter.MyViewHolder holder, int position) {
        String title = moviesArrayList.get(position).getTitle();
        String releaseDate = moviesArrayList.get(position).getRelease_date();
        String posterPath = moviesArrayList.get(position).getPoster_path();

        holder.title.setText(title);
        holder.release_date.setText(releaseDate);
        Glide.with(holder.poster_path.getContext()).load(posterPath).into(holder.poster_path);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

}