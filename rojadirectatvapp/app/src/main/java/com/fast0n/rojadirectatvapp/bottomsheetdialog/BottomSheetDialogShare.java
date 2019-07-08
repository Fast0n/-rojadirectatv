package com.fast0n.rojadirectatvapp.bottomsheetdialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fast0n.rojadirectatvapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogShare extends BottomSheetDialogFragment {

    String stringShare;

    public BottomSheetDialogShare(String infoShare) {
        stringShare = infoShare;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_share, container, false);
        inflater.getContext().setTheme(R.style.BottomSheetDialogTheme);


        ConstraintLayout share;


        share = view.findViewById(R.id.fragment_history_bottom_sheet_share);

        share.setOnClickListener(view12 -> {
            String _name = stringShare.split("::")[1];
            String time = stringShare.split("::")[2];
            String url = stringShare.split("::")[3];


            String share1 =
                    _name.toUpperCase() + "\n🕜 "
                            + "" + " " + time + "\n\n" + url;

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, share1);
            view12.getContext().startActivity(Intent.createChooser(sharingIntent, view12.getContext().getString(R.string.share)));
            dismiss();
        });


        return view;
    }
}
