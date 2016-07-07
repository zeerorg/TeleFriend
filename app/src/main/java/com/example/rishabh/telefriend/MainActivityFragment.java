package com.example.rishabh.telefriend;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        final EditText searchText = (EditText) v.findViewById(R.id.search_text);
        Button searchButton = (Button) v.findViewById(R.id.button);

        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Toast.makeText(getActivity(), searchText.getText(), Toast.LENGTH_LONG).show();
                if(searchText.getText().toString() != "") {
                    Intent intent = new Intent(getActivity(), Data.class);
                    intent.putExtra(Intent.EXTRA_TEXT, searchText.getText().toString());
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_frag, menu);
    }
}
