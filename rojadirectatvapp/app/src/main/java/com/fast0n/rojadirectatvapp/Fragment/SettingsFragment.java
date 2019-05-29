package com.fast0n.rojadirectatvapp.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fast0n.rojadirectatvapp.BuildConfig;
import com.fast0n.rojadirectatvapp.R;
import com.fast0n.rojadirectatvapp.SnackbarHelper;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String theme;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView info = view.findViewById(R.id.info);
        Switch aSwitch = view.findViewById(R.id.switch1);

        settings = getContext().getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        theme = settings.getString("toggleTheme", null);

        if (theme.equals("0"))
            aSwitch.setChecked(false);
        else
            aSwitch.setChecked(true);


        Handler handler = new Handler();
        final Runnable runnable = () -> {
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(getActivity().getIntent());
            getActivity().overridePendingTransition(0, 0);

        };

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("toggleTheme", "1");
                    editor.apply();
                    handler.postDelayed(runnable, 500);
                } else {
                    editor.putString("toggleTheme", "0");
                    editor.apply();
                    handler.postDelayed(runnable, 500);

                }
            }
        });

        String about_string = String.format(getResources().getString(R.string.titleTextView_fragment_settings), getResources().getString(R.string.app_name));
        String version_string = String.format(getResources().getString(R.string.version), getResources().getString(R.string.app_name));
        info.setText(about_string);

        info.setOnClickListener(view1 -> {

            Snackbar snack = Snackbar.make(
                    view1,
                    version_string + " " + BuildConfig.VERSION_NAME + "\n(" + BuildConfig.VERSION_CODE + ")",
                    Snackbar.LENGTH_LONG
            );
            SnackbarHelper.configSnackbar(view1.getContext(), snack);
            snack.setActionTextColor(getResources().getColor(android.R.color.black)).setAction("Copia", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copied", BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");
                    clipboard.setPrimaryClip(clip);

                }
            });
            snack.show();


        });


        return view;
    }
}
