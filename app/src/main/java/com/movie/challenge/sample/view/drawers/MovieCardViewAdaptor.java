package com.movie.challenge.sample.view.drawers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.challenge.sample.DetailActivity;
import com.movie.challenge.sample.R;
import com.movie.challenge.sample.raw.structures.TV_Movie;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieCardViewAdaptor extends RecyclerView.Adapter<MovieCardViewAdaptor.MovieViewHolder> {

    Context mContext;
    ArrayList<TV_Movie> movies;
    //TV_Movie focused_movie;
    int positionHolder;
    private ImageLoadTask mAuthTask = null;
    String TITLE = "TITLE";
    String RELEASE = "RELEASE";
    String POSTER = "POSTER";
    String OVERVIEW = "OVERVIEW";

    public MovieCardViewAdaptor(Context mContext, ArrayList<TV_Movie> movies) {
        this.mContext = mContext;
        this.movies = movies;
    }

    @Override
    public MovieCardViewAdaptor.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        MovieViewHolder pvh = new MovieViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MovieCardViewAdaptor.MovieViewHolder holder, int position) {

        mAuthTask = new ImageLoadTask(movies.get(position).getmPoster(),holder.poster);
        mAuthTask.execute();

        holder.title.setText(movies.get(position).getmTitle());
        holder.overview.setText(movies.get(position).getmOverview());
        holder.release.setText(movies.get(position).getmReleaseDate());

        final TV_Movie focused_movie =  movies.get(position);
        positionHolder = position;

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(TITLE, focused_movie.getmTitle());
                intent.putExtra(RELEASE, focused_movie.getmReleaseDate());
                intent.putExtra(POSTER, focused_movie.getmPoster());
                intent.putExtra(OVERVIEW, focused_movie.getmOverview());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView poster;
        TextView title;
        TextView overview;
        TextView release;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_movie);
            poster = (ImageView)itemView.findViewById(R.id.poster_adding);
            title = (TextView)itemView.findViewById(R.id.upper_adding);
            overview = (TextView)itemView.findViewById(R.id.lower_adding);
            release = (TextView)itemView.findViewById(R.id.released_date);
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mAuthTask = null;
            if(result == null){
                imageView.setImageResource(R.drawable.noimage);
            } else {
                imageView.setImageBitmap(result);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }

}
