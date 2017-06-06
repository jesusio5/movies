package com.movie.challenge.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.movie.challenge.sample.backend.callers.HttpCaller;
import com.movie.challenge.sample.raw.structures.TV_Movie;
import com.movie.challenge.sample.view.drawers.MovieCardViewAdaptor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static MovieCardViewAdaptor movieviewer;
    public static ArrayList<TV_Movie> tv_movies;
    public static String lastQuery;
    HttpCaller httpCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastQuery = "";
        tv_movies = new ArrayList<>();
        httpCaller = new HttpCaller();

        Toolbar toolbar = (Toolbar) findViewById(R.id.adding_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView moviescase = (RecyclerView)findViewById(R.id.seriescase);
        moviescase.setLayoutManager(( new LinearLayoutManager(this)));
        moviescase.setAdapter( (movieviewer = new MovieCardViewAdaptor(this,tv_movies)));
    }

    public void initSearch(View view){
        String editTextQuery = ((EditText) findViewById(R.id.txt_search)).getText().toString();
        if(!editTextQuery.equals("") && !editTextQuery.equals(lastQuery)){
            lastQuery = editTextQuery;
            httpCaller.CardViewTaskWrapper(this,movieviewer,tv_movies,lastQuery);
        } else {
            Toast.makeText(this,"Please type a word to search.",Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshRecicler(){
        movieviewer.notifyDataSetChanged();
    }

}
