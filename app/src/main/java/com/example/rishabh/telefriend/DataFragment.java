package com.example.rishabh.telefriend;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * A placeholder fragment containing a simple view.
 */
public class DataFragment extends Fragment {

    public static View v;

    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_data, container, false);
        Intent intent = getActivity().getIntent();
        String toSearch = intent.getExtras().getString(Intent.EXTRA_TEXT);
        FetchData fetch = new FetchData();
        fetch.execute(toSearch);
        return v;
    }

    public static void updatePage(String titleText, String imdbRating, String tomatoRating, String Year){
        TextView imdb  = (TextView) v.findViewById(R.id.imdb);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView rotten = (TextView) v.findViewById(R.id.rotten);
        title.setText(titleText+" ("+Year+")");
        imdb.setText(imdbRating);
        rotten.setText(tomatoRating);
        /*
        String url = "http://ia.media-imdb.com/images/M/MV5BMDM5NjVlYTEtMGQ4Yy00OTk2LWJmMzEtZDkxYjNkMjY5YTVjXkEyXkFqcGdeQXVyNjEyOTQ3ODU@._V1_SX300.jpg";

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true).build();

//initialize image view
        ImageView imageView = (ImageView) v.findViewById(R.id.thumbView);

//download and display image from url
        imageLoader.displayImage(url, imageView, options);
        */
    }
}

class FetchData extends AsyncTask<String, Void, String>{

    @Override
    public String doInBackground(String... params) {
        /*
        Uri.Builder buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("www.omdbapi.com")
                .appendQueryParameter("t", params[0])
                .appendQueryParameter("tomatoes", "true");

        String link = buildURL.build().toString();
        Log.e("FetchData", link);

        URL url;
        HttpURLConnection conn;
        InputStream is;
        BufferedReader buffer;
        StringBuffer jsondata = new StringBuffer("");

        try {
            url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            Log.v("FetchData", "HttpUrlConnection working");
            is = conn.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while((line = buffer.readLine()) != null){
                jsondata.append(line + "\n");
            }
            conn.disconnect();
            Log.e("FetchData", jsondata.toString());
            return jsondata.toString();
        }
        catch(Exception e){
            Log.e("FetchData", "Did not connect");
            return "";
        }
        */

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            Uri.Builder buildURL = new Uri.Builder();
            buildURL.scheme("http")
                    .authority("www.omdbapi.com")
                    .appendQueryParameter("t", params[0])
                    .appendQueryParameter("tomatoes", "true");

            URL url = new URL(buildURL.build().toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
        return forecastJsonStr;


    }

    public void onPostExecute(String jsondata){
        try {
            JSONObject json = new JSONObject(jsondata);
            Log.e("FetchData convert json", jsondata.toString());
            DataFragment.updatePage(json.getString("Title"),
                    json.getString("imdbRating"),
                    json.getString("tomatoRating"),
                    json.getString("Year")
            );
        } catch (JSONException e) {
            Log.e("FetchData", e.toString());
             //DataFragment.TITLE = "";
        }
    }
}

//TODO: Implement Universal Image Loader