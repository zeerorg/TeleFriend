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

    public static void updatePage(String titleText, String imdbRating){
        TextView imdb  = (TextView) v.findViewById(R.id.imdb);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(titleText);
        imdb.setText(imdbRating);
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
            DataFragment.updatePage(json.getString("Title"), json.getString("imdbRating"));
        } catch (JSONException e) {
            Log.e("FetchData", e.toString());
             //DataFragment.TITLE = "";
        }
    }
}
