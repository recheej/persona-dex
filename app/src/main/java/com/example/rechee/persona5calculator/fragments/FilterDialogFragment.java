package com.example.rechee.persona5calculator.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.models.ArcanaMap;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.PersonaFilterArgs;
import com.example.rechee.persona5calculator.viewmodels.PersonaFilterViewModel;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class FilterDialogFragment extends DialogFragment {

    public  interface OnFilterListener {
        void onFilterSelected(PersonaFilterArgs filterArgs);
    }

    @Inject
    PersonaFilterViewModel viewModel;

    private BaseActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.activity = (BaseActivity) getActivity();
        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        LayoutInflater inflater = activity.getLayoutInflater();

        final View view = inflater.inflate(R.layout.filter_dialog, null);

        final Spinner arcanaSpinner = (Spinner) view.findViewById(R.id.spinner_arcana);

        ArrayAdapter<ArcanaMap> arcanaMapArrayAdapter = new ArrayAdapter<>(activity,
                R.layout.support_simple_spinner_dropdown_item, viewModel.getArcanaMaps());
        arcanaSpinner.setAdapter(arcanaMapArrayAdapter);

        InputFilter filter = new InputFilter.LengthFilter(2);

        final EditText minLevelEditText = (EditText) view.findViewById(R.id.editText_minLevel);
        final EditText maxLevelEditText = (EditText) view.findViewById(R.id.editText_maxLevel);

        minLevelEditText.setFilters(new InputFilter[]{filter});
        maxLevelEditText.setFilters(new InputFilter[]{filter});

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(view)
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PersonaFilterArgs filterArgs = new PersonaFilterArgs();

                        ArcanaMap selectedArcanaMap = (ArcanaMap) arcanaSpinner.getSelectedItem();
                        filterArgs.arcana = selectedArcanaMap.arcana;


                        String minLevelText = minLevelEditText.getText().toString();
                        if(!minLevelText.isEmpty()){
                            filterArgs.minLevel = Integer.parseInt(minLevelText);
                        }

                        String maxLevelText = maxLevelEditText.getText().toString();
                        if(!minLevelText.isEmpty()){
                            filterArgs.maxLevel = Integer.parseInt(maxLevelText);
                        }

                        OnFilterListener listener = (OnFilterListener) activity;
                        listener.onFilterSelected(filterArgs);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            }
        });

        return  alertDialog;
    }
}
