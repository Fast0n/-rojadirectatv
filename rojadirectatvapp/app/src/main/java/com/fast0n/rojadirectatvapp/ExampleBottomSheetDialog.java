package com.fast0n.rojadirectatvapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {

    String stringShare;

    public ExampleBottomSheetDialog(String infoShare) {
        stringShare = infoShare;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);


        LinearLayout close, share;

        close = view.findViewById(R.id.close);

        share = view.findViewById(R.id.fragment_history_bottom_sheet_share);

        share.setOnClickListener(view12 -> {
            String _name = stringShare.split("::")[1];
            String time = stringShare.split("::")[2];


            String share1 =
                    _name.toUpperCase() + "\n🕜 "
                            + "" + " " + time;

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, share1);
            view12.getContext().startActivity(Intent.createChooser(sharingIntent, view12.getContext().getString(R.string.share)));
            dismiss();
        });


        close.setOnClickListener(view1 -> dismiss());


        return view;
    }
}
