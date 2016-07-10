package com.example.rishabh.telefriend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.androidquery.AQuery;

/**
 * A placeholder fragment containing a simple view.
 */
public class DataFragment extends Fragment {

    public static View v;
    public static AQuery aq;

    public DataFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_data, container, false);
        aq = new AQuery(getActivity(), v);
        Intent intent = getActivity().getIntent();
        String toSearch = intent.getExtras().getString(Intent.EXTRA_TEXT);

        FetchData fetch = new FetchData();
        fetch.execute(toSearch);
        /*
        ImageView thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        thumbnail.setImageResource(R.mipmap.ic_launcher);
        */
        return v;
    }


    public static void updatePage(String titleText, String imdbRating, String tomatoRating, String Year, String image){

        TextView imdb  = (TextView) v.findViewById(R.id.imdb);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView rotten = (TextView) v.findViewById(R.id.rotten);
        title.setText(titleText+" ("+Year+")");
        imdb.setText(imdbRating);
        rotten.setText(tomatoRating+"%");

        aq.id(R.id.thumbnail).image(image, false, false);
    }
}

class FetchData extends AsyncTask<String, Void, String>{

    @Override
    public String doInBackground(String... params) {
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
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.e("FetchData", "inputStream is null");
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
            Log.e("FetchData", "IOException: "+e.toString());
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
                    json.getString("tomatoUserMeter"),
                    json.getString("Year"),
                    json.getString("Poster")
                    //getImage(json.getString("Poster"))
                    //null
            );
        } catch (Exception e) {
            Log.e("FetchData", e.toString());
             //DataFragment.TITLE = "";
        }
    }
}