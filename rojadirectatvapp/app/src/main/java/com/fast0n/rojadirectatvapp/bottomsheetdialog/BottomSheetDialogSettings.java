package com.fast0n.rojadirectatvapp.bottomsheetdialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fast0n.rojadirectatvapp.BuildConfig;
import com.fast0n.rojadirectatvapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogSettings extends BottomSheetDialogFragment {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String theme;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_settings, container, false);
        inflater.getContext().setTheme(R.style.BottomSheetDialogTheme);

        TextView info = view.findViewById(R.id.info);
        Switch aSwitch = view.findViewById(R.id.switch1);
        ConstraintLayout bottom_sheet_theme = view.findViewById(R.id.bottom_sheet_theme);
        ConstraintLayout bottom_sheet_info = view.findViewById(R.id.bottom_sheet_info);


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


        String about_string = String.format(getResources().getString(R.string.titleTextView_fragment_settings), getResources().getString(R.string.app_name));
        info.setText(about_string);

        bottom_sheet_info.setOnClickListener(view12 -> {

            String version_string = String.format(getResources().getString(R.string.version), getResources().getString(R.string.app_name));

            Toast.makeText(view12.getContext(), version_string + " " + BuildConfig.VERSION_NAME + "\n(" + BuildConfig.VERSION_CODE + ")", Toast.LENGTH_LONG).show();


            dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*

                    Snackbar snack = Snackbar.make(
                    view12,
                    version_string + " " + BuildConfig.VERSION_NAME + "\n(" + BuildConfig.VERSION_CODE + ")",
                    Snackbar.LENGTH_LONG
            );

            SnackbarHelper.configSnackbar(view12.getContext(), snack);
            snack.setActionTextColor(getResources().getColor(android.R.color.black)).setAction("Copia", v -> {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied", BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");
                clipboard.setPrimaryClip(clip);

            });
                                        snack.show();

                     */

                }
            }, 200);

        });


        bottom_sheet_theme.setOnClickListener(view1 -> {
            if (theme.equals("0")) {
                aSwitch.setChecked(true);
                editor.putString("toggleTheme", "1");
                editor.apply();
                handler.postDelayed(runnable, 500);
            } else {
                aSwitch.setChecked(false);
                editor.putString("toggleTheme", "0");
                editor.apply();
                handler.postDelayed(runnable, 500);
            }


        });


        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                editor.putString("toggleTheme", "1");
                editor.apply();
                handler.postDelayed(runnable, 500);
            } else {
                editor.putString("toggleTheme", "0");
                editor.apply();
                handler.postDelayed(runnable, 500);

            }
        });


        return view;
    }


}
