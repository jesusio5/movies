package com.movie.challenge.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.challenge.sample.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private ImageLoadTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle aux = getIntent().getExtras();
        mAuthTask = new ImageLoadTask(aux.getString("POSTER"),
                (ImageView) findViewById(R.id.img_up));
        mAuthTask.execute();
        TextView title = (TextView) findViewById(R.id.etxt_title);
        TextView release = (TextView) findViewById(R.id.etxt_release);
        TextView overview = (TextView) findViewById(R.id.etxt_overview);
        title.setText(aux.getString("TITLE"));
        release.setText(aux.getString("RELEASE"));
        overview.setText(aux.getString("OVERVIEW"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.returning_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
