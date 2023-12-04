package com.example.prova_android_n2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.Handler;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    Handler mainHandler = new Handler();
    ArrayList<Movie> moviesArrayList = new ArrayList<>();
    ArrayList<Movie> moviesArrayListCopia = new ArrayList<Movie>();
    MinhaClasseRecyclerAdapter minhaClasseRecyclerAdapter;
    SearchView searchView;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        popularArrayList();
        configurarAdapter();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                moviesArrayListCopia.addAll(moviesArrayList);
                moviesArrayList.clear();

                if(s.isEmpty()){
                    moviesArrayList.addAll(moviesArrayListCopia);
                }
                else{
                    s = s.toLowerCase();
                    for(Movie item: moviesArrayListCopia){
                        if(item.getTitle().toLowerCase().contains(s)){
                            moviesArrayList.add(item);
                        }
                    }
                    minhaClasseRecyclerAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        //efeito visual de snap - exibe parte do elemento que está além do limite da tela
        SnapHelper snapHelper = new LinearSnapHelper(); // ou PagerSnapHelper(); para simular ViewPager - elemento tela toda
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void configurarAdapter() {
        minhaClasseRecyclerAdapter = new MinhaClasseRecyclerAdapter(moviesArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(minhaClasseRecyclerAdapter);
    }

    private void popularArrayList() {
        String base_path = "https://image.tmdb.org/t/p/w500";

        moviesArrayList.add(new Movie(1076364, "O Encontro de Carl", base_path + "/81Uq4CEUPRpbLrSPOREpQJFdFFr.jpg", "2023-06-15"));
        moviesArrayList.add(new Movie(580489, "Venom: Let There Be Carnage", base_path + "/h5UzYZquMwO9FVn15R2eK2itmHu.jpg", "2021-09-30"));
        moviesArrayList.add(new Movie(22, "Piratas do Caribe: A Maldição do Pérola Negra", base_path + "/9Xcg7Ar4ketv4rl8yeK32yp9zQA.jpg", "2003-07-09"));
    }

    private void formatarDados(String texto) {
        try {
            JSONArray jsonArray = new JSONArray(texto);
            JSONArray moviesArray = jsonArray.getJSONObject(0).getJSONArray("results");

            for (int i = 0; moviesArray.length() > i; i++) {
                JSONObject movieJSObj = jsonArray.getJSONObject(i);
                Movie movie = new Movie();

                long movie_id = Long.parseLong(movieJSObj.getString("id"));
                movie.setId(movie_id);

                String title = movieJSObj.getString("title");
                movie.setTitle(title);

                String release_date = movieJSObj.getString("release_date");
                movie.setRelease_date(release_date);

                String poster_path = movieJSObj.getString("poster_path");
                movie.setPoster_path(poster_path);

                moviesArrayList.add(movie);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    class FetchData extends Thread {
        String data = "";
        @Override
        public void run() {
            try {
                URL url = null;
                url = new URL("https://api.themoviedb.org/3/search/movie?api_key=673b69ec0f095b0b5a832990ad639b97&query=Car&language=pt-BR&page=1&include_adult=false");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;

                while((linha = bufferedReader.readLine()) != null){
                    data += linha;
                }
                if(!data.isEmpty()){
                    formatarDados(data);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}