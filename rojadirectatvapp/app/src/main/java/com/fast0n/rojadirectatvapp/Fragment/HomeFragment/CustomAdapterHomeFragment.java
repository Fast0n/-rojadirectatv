package com.fast0n.rojadirectatvapp.Fragment.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fast0n.rojadirectatvapp.ExampleBottomSheetDialog;
import com.fast0n.rojadirectatvapp.R;

import java.util.ArrayList;

public class CustomAdapterHomeFragment extends ArrayAdapter<DataItems> {

    String name;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String theme;
    private Context context;

    CustomAdapterHomeFragment(Context context, ArrayList<DataItems> data) {
        super(context, R.layout.row_item, data);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataItems dataItems = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.row_item, parent, false);


            // addressed
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.txtTime = convertView.findViewById(R.id.time);
            viewHolder.txtStatus = convertView.findViewById(R.id.url_status);
            ViewHolder.share = convertView.findViewById(R.id.share);
            ViewHolder.btnOpenURL = convertView.findViewById(R.id.btnOpenURL);
            viewHolder.url_link = convertView.findViewById(R.id.url_link);
            viewHolder.option_more = convertView.findViewById(R.id.option_more);


            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        ViewHolder.btnOpenURL.setOnClickListener(view -> {
            //ciao
            context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(viewHolder.url_link.getText().toString())));

        });
        viewHolder.txtName.setText(dataItems.getName());


        settings = getContext().getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        theme = settings.getString("toggleTheme", null);


        int mColor;
        if (theme.equals("0"))
            mColor = Color.parseColor("#F5F5F5");
        else
            mColor = Color.parseColor("#212121");

        ViewHolder.share.setOnClickListener(view -> {


            name = viewHolder.option_more.getText().toString();

            if (name.split("::")[0].equals("options")) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                ExampleBottomSheetDialog bottomSheetDialog = new ExampleBottomSheetDialog(viewHolder.option_more.getText().toString());
                bottomSheetDialog.show(activity.getSupportFragmentManager(), "show");
            }

        });


        viewHolder.url_link.setText(dataItems.getUrl());
        viewHolder.txtTime.setText(dataItems.getTime());
        viewHolder.txtStatus.setText(dataItems.getUrl_status());


        viewHolder.option_more.setText("options" + "::" +
                dataItems.getName() + "::" +
                dataItems.getTime() + "::");

        return convertView;
    }

    private static class ViewHolder {
        static Button btnOpenURL;
        static ImageButton share;
        TextView txtName, txtTime, txtStatus, url_link, option_more;
    }
}


