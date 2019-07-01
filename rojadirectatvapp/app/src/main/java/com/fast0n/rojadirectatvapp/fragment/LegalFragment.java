package com.fast0n.rojadirectatvapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.rojadirectatvapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LegalFragment extends Fragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal, container, false);

        textView = view.findViewById(R.id.tv_desc);
        String url = getResources().getString(R.string.url_legal);

        try {
            if (isOnline()) {
                //get(url, view, 0);
                get(url, view, 1);
            } else
                get(url, view, 1);
        } catch (Exception e) {
            get(url, view, 0);
        }


        return view;


    }

    private void get(String url, View view1, int i) {


        if (i == 1) {

            try {
                JSONArray json_raw, jsonArray1;
                String jsonCredit = PreferenceManager.
                        getDefaultSharedPreferences(view1.getContext()).getString("legal", null);


                jsonArray1 = new JSONArray(jsonCredit);
                json_raw = jsonArray1;


                makeList(json_raw, view1, textView);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {


            RequestQueue queue = Volley.newRequestQueue(view1.getContext());
            // Initialize a new JsonArrayRequest instance
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {


                        PreferenceManager.getDefaultSharedPreferences(view1.getContext()).edit()
                                .remove("legal").apply();


                        PreferenceManager.getDefaultSharedPreferences(view1.getContext()).edit()
                                .putString("legal", response.toString()).apply();

                        try {
                            JSONArray json_raw = new JSONArray(response.toString());
                            makeList(json_raw, view1, textView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    },
                    error -> {
                        //ckdidi
                        Log.e("strn:", error.toString());

                    }
            );

            // Add JsonArrayRequest to the RequestQueue
            queue.add(jsonArrayRequest);
        }
    }

    private void makeList(JSONArray json_raw, View view1, TextView textView) {
        try {


            String partenza2 = json_raw.getString(0);
            JSONObject scorrOrari = new JSONObject(partenza2);
            String str_text = scorrOrari.getString("text");

            textView.setText(Html.fromHtml(str_text));


        } catch (JSONException e) {

            Log.e("strn:", e.toString());
        }


    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
