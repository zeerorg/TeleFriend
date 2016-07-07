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

/**
 * A placeholder fragment containing a simple view.
 */
public class DataFragment extends Fragment {

    public static String TITLE = new String("Title");
    public static String IMDB = new String("0.0");
    public static String ROTTEN = new String("");
    public static TextView imdb;
    public static TextView title;

    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        Intent intent = getActivity().getIntent();
        String toSearch = intent.getExtras().getString(Intent.EXTRA_TEXT);
        FetchData fetch = new FetchData();
        fetch.execute(toSearch);

        title = (TextView) v.findViewById(R.id.title);
        imdb = (TextView) v.findViewById(R.id.imdb);

        title.setText(TITLE);
        imdb.setText(IMDB);
        return v;
    }
}

class FetchData extends AsyncTask<String, Void, String>{

    @Override
    public String doInBackground(String... params) {
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
    }

    public void onPostExecute(String jsondata){
        try {
            JSONObject json = new JSONObject(jsondata);
            Log.e("FetchData convert json", jsondata.toString());
            DataFragment.TITLE = json.getString("Title");
            DataFragment.IMDB = json.getString("imdbRating");
            DataFragment.title.setText(DataFragment.TITLE);
            DataFragment.imdb.setText(DataFragment.IMDB);
        } catch (JSONException e) {
            Log.e("FetchData", e.toString());
             //DataFragment.TITLE = "";
        }
    }
}
