package com.example.prova_android_n2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MinhaClasseRecyclerAdapter extends RecyclerView.Adapter<MinhaClasseRecyclerAdapter.MyViewHolder> {

    ArrayList<Movie> moviesArrayList = new ArrayList<>();

    public MinhaClasseRecyclerAdapter(ArrayList<Movie> usuarioArrayList) {
        this.moviesArrayList = usuarioArrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView release_date;
        ImageView poster_path;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            release_date = itemView.findViewById(R.id.tvReleaseDate);
            poster_path = itemView.findViewById(R.id.imgPoster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(itemView.getContext(), "Removido", Toast.LENGTH_SHORT).show();

            moviesArrayList.remove(getLayoutPosition());
            notifyItemRemoved(getLayoutPosition());
            notifyItemRangeChanged(getLayoutPosition(), moviesArrayList.size());
        }
    }

    @NonNull
    @Override
    public MinhaClasseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.menu_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MinhaClasseRecyclerAdapter.MyViewHolder holder, int position) {
        String title = moviesArrayList.get(position).getTitle();
        String release_date = moviesArrayList.get(position).getRelease_date();
        String poster_path = moviesArrayList.get(position).getPoster_path();

        holder.title.setText(title);
        holder.release_date.setText(release_date);
        //holder.poster_path.setImageURI(Uri.parse(poster_path));
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }
}