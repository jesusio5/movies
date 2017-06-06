package com.movie.challenge.sample.backend.callers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.movie.challenge.sample.view.drawers.MovieCardViewAdaptor;
import com.movie.challenge.sample.raw.structures.TV_Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpCaller {
    private final String BASE_URL = "https://api.themoviedb.org";
    private final String SEARCH_MOVIE = "/3/search/movie";
    private final String SEARCH_PHOTO = "https://image.tmdb.org/t/p/w500/";
    private final String APIKEY = "09eed07eb7934c07c3b1d23b77300364";


    public void CardViewTaskWrapper(Context mContext,MovieCardViewAdaptor movieList,
                                    ArrayList<TV_Movie> movies,String query){
        new CardViewsTask(mContext,BASE_URL,SEARCH_MOVIE,
                SEARCH_PHOTO,APIKEY,movieList,movies,query).execute();
    }


    public class CardViewsTask extends AsyncTask<Void, Void, Void> {

        private final String mUrl;
        private final String mUrlCallback_search_movie;
        private final String mUrlCallback_search_poster;
        private final String api_Token;
        Context mContext;
        ProgressDialog proDialog;
        MovieCardViewAdaptor mMovieList;
        ArrayList<TV_Movie> mMovies;
        String mQuery;

        CardViewsTask(Context context,
                      String url, String urlCallback_search_movie, String urlCallback_search_poster,
                      String security_Token, MovieCardViewAdaptor movieList, ArrayList<TV_Movie> movies,
                      String query) {
            mContext = context;
            mUrl = url;
            mUrlCallback_search_movie = urlCallback_search_movie;
            mUrlCallback_search_poster = urlCallback_search_poster;
            api_Token = security_Token;
            mMovieList = movieList;
            mMovies = movies;
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(mContext);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                String uri = mUrl + mUrlCallback_search_movie;
                String urlParameters = ("?api_key=" + api_Token + "&query=" + mQuery).replace(' ','+');
                url = new URL(uri + urlParameters);
                HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
                setProperConectionConfiguration(conn);
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String inputLine;
                    String result = "";
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        result += inputLine;
                    }
                    parseIntoRecycler(result);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            if (proDialog.isShowing())
                proDialog.dismiss();

            mMovieList.notifyDataSetChanged();
        }

        private void setProperConectionConfiguration(HttpsURLConnection conn) throws
                NoSuchAlgorithmException, KeyManagementException, ProtocolException {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setConnectTimeout(15001);
            conn.setReadTimeout(15001);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
        }

        private void parseIntoRecycler(String json){
            JSONObject rawObject;
            try {
                rawObject = new JSONObject(json);
                JSONArray results = rawObject.getJSONArray("results");
                mMovies.clear();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject aux_movie_json = results.getJSONObject(i);
                    String poster_path = mUrlCallback_search_poster +
                            aux_movie_json.getString("poster_path").substring(1);
                    String overview = aux_movie_json.getString("overview");
                    String release_date = aux_movie_json.getString("release_date");
                    String title = aux_movie_json.getString("title");
                    TV_Movie aux = new TV_Movie(
                            title,
                            poster_path,
                            release_date,
                            overview
                    );
                    mMovies.add(
                            aux
                    );
                }
            } catch(JSONException e){
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(),"Something went wrong with the returned json tried to parse.");
            }
        }

    }

}
