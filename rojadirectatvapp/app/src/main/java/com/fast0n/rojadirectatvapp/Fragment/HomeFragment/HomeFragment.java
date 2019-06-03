package com.fast0n.rojadirectatvapp.Fragment.HomeFragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    CustomAdapterHomeFragment adapter;
    ProgressBar pb;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pb = getActivity().findViewById(R.id.progressBar);
        pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
        String url = getResources().getString(R.string.url_home);

        try {
            if (isOnline()) {
                get(url, view, 0);
                get(url, view, 1);
                pb.setVisibility(View.VISIBLE);
            } else
                get(url, view, 1);
        } catch (Exception e) {
            get(url, view, 0);
        }


        return view;
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    private void get(String url, View view1, int i) {


        ListView listView = view1.findViewById(R.id.list);


        if (i == 1) {
            try {
                JSONArray json_raw, jsonArray1;
                String jsonCredit = PreferenceManager.
                        getDefaultSharedPreferences(view1.getContext()).getString("credit", null);


                jsonArray1 = new JSONArray(jsonCredit);
                json_raw = jsonArray1;

                ArrayList<DataItems> dataHours = new ArrayList<>();


                makeList(dataHours, json_raw, view1, listView);


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
                                .remove("credit").apply();


                        PreferenceManager.getDefaultSharedPreferences(view1.getContext()).edit()
                                .putString("credit", response.toString()).apply();


                        try {
                            JSONArray json_raw = new JSONArray(response.toString());
                            ArrayList<DataItems> dataHours = new ArrayList<>();
                            makeList(dataHours, json_raw, view1, listView);

                        } catch (JSONException ignored) {


                        }


                    },
                    error -> {
                        //ckdidi
                        Log.e("strn:", error.toString());

                    }
            );

            // Add JsonArrayRequest to the RequestQueue
            queue.add(jsonArrayRequest);


            //fine
        }
    }

    private void makeList(ArrayList<DataItems> dataHours, JSONArray json_raw, View view1, ListView listView) {

        try {
            for (int j = 0; j < json_raw.length(); j++) {
                String partenza2 = json_raw.getString(j);
                JSONObject scorrOrari = new JSONObject(partenza2);
                String str_name = scorrOrari.getString("name");
                String str_time = scorrOrari.getString("time");
                String str_url = scorrOrari.getString("url");
                String str_icon = scorrOrari.getString("icon");
                String str_status = scorrOrari.getString("url_status");
                String _class = scorrOrari.getString("class");

                if (str_status.equals("0"))
                    str_status = "⛔️"+getString(R.string.status);
                else
                    str_status = "✅"+getString(R.string.sstatus);

                if (str_time.equals("24/7"))
                    dataHours.add(new DataItems(str_name, str_time, str_url, str_status));
                else
                    dataHours.add(new DataItems(str_icon + " " + _class + ": " + str_name, getString(R.string.start) + " " + str_time, str_url, str_status));
            }


            adapter = new CustomAdapterHomeFragment(view1.getContext(), dataHours);

            listView.setAdapter(adapter);
            pb.setVisibility(View.GONE);
        } catch (JSONException e) {
            Log.e("strn:", e.toString());

        }


    }


}
