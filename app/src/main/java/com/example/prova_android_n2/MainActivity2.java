package com.example.prova_android_n2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    ArrayList<Movie> moviesArrayList = new ArrayList<>();
    ArrayList<Movie> moviesArrayListCopia = new ArrayList<>();
    MinhaClasseRecyclerAdapter minhaClasseRecyclerAdapter;
    RecyclerView recyclerView;
    SearchView searchView_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);
        searchView_ = findViewById(R.id.searchView);

        //searchView aberto
        searchView_.setIconified(false);
        //retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView_.clearFocus();
        // Configuração do RecyclerView e Adapter
        configurarAdapter();

        // Chamada para buscar e processar dados da API
        String query = "https://api.themoviedb.org/3/discover/movie?api_key=673b69ec0f095b0b5a832990ad639b97&language=pt-BR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate";
        buscarDadosAPI(query);
    }

    private void configurarAdapter() {
        minhaClasseRecyclerAdapter = new MinhaClasseRecyclerAdapter(moviesArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(minhaClasseRecyclerAdapter);
    }

    private void buscarDadosAPI(String query) {
        Thread fetchDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(query);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String linha;

                    while ((linha = bufferedReader.readLine()) != null) {
                        stringBuilder.append(linha);
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    String response = stringBuilder.toString();

                    if (response != null && !response.isEmpty()) {
                        formatarDados(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        fetchDataThread.start();

        searchView_.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filtrar(s);
                minhaClasseRecyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtrar(s);
                minhaClasseRecyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void filtrar(String text) {
        moviesArrayList.clear();

        if (text.isEmpty()) {
            moviesArrayList.addAll(moviesArrayListCopia);
        } else {
            text = text.toLowerCase();
            for (Movie item : moviesArrayListCopia) {
                if (item.toString().toLowerCase().contains(text)) {
                    moviesArrayList.add(item);
                }
            }
        }
    }
    private void formatarDados(String texto) {
        Log.e("texto", texto);
        String base_path = "https://image.tmdb.org/t/p/w500";

        try {
            JSONObject jsonObject = new JSONObject(texto);
            JSONArray moviesArray = jsonObject.getJSONArray("results");
            final ArrayList<Movie> moviesArrayListFormatted = new ArrayList<>();

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieJSObj = moviesArray.getJSONObject(i);
                Movie movie = new Movie();

                long movie_id = movieJSObj.getLong("id");
                movie.setId(movie_id);

                String title = movieJSObj.getString("title");
                movie.setTitle(title);

                String release_date = movieJSObj.getString("release_date");
                movie.setRelease_date(release_date);

                String poster_path = movieJSObj.getString("poster_path");
                movie.setPoster_path(base_path + poster_path);

                moviesArrayListFormatted.add(movie);
            }

            // Atualiza a lista moviesArrayList original na thread principal usando o Handler
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    moviesArrayList.clear();
                    moviesArrayList.addAll(moviesArrayListFormatted);
                    moviesArrayListCopia.addAll(moviesArrayList);
                    minhaClasseRecyclerAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
