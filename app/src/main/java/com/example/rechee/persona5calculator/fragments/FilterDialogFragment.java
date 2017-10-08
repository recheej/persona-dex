package com.example.rechee.persona5calculator.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private final int ANY_ARCANA_ORDINAL = -1;
    private CheckBox rarePersonaCheckBox;
    private CheckBox dlcPersonaCheckBox;
    private EditText minLevelEditText;
    private EditText maxLevelEditText;
    private ArcanaMap selectedArcanaMap;
    private Spinner arcanaSpinner;
    private ArrayAdapter<ArcanaMap> arcanaMapArrayAdapter;

    public  interface OnFilterListener {
        void onFilterSelected(PersonaFilterArgs filterArgs);
    }

    @Inject
    PersonaFilterViewModel viewModel;

    private BaseActivity activity;

    public static FilterDialogFragment newInstance(PersonaFilterArgs args){
        //used to restore the fragment's state since the fragment's onRestore and onSave aren't called
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        if(args != null){
            Bundle bundleArguments = new Bundle();

            bundleArguments.putBoolean("rarePersona", args.rarePersona);
            bundleArguments.putBoolean("dlcPersona", args.dlcPersona);
            bundleArguments.putInt("selectedArcana", args.arcanaOrdinal);
            bundleArguments.putInt("minLevel", args.minLevel);
            bundleArguments.putInt("maxLevel", args.maxLevel);

            filterDialogFragment.setArguments(bundleArguments);
        }

        return filterDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.activity = (BaseActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = this.activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.filter_dialog, null);

        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        arcanaSpinner = (Spinner) view.findViewById(R.id.spinner_arcana);

        arcanaMapArrayAdapter = new ArrayAdapter<>(activity,
                R.layout.support_simple_spinner_dropdown_item, viewModel.getArcanaMaps());
        arcanaSpinner.setAdapter(arcanaMapArrayAdapter);

        InputFilter filter = new InputFilter.LengthFilter(2);

        minLevelEditText = (EditText) view.findViewById(R.id.editText_minLevel);
        maxLevelEditText = (EditText) view.findViewById(R.id.editText_maxLevel);

        minLevelEditText.setFilters(new InputFilter[]{filter});
        maxLevelEditText.setFilters(new InputFilter[]{filter});

        rarePersonaCheckBox = (CheckBox) view.findViewById(R.id.checkbox_rarePersona);
        dlcPersonaCheckBox = (CheckBox) view.findViewById(R.id.checkbox_dlcPersona);

        builder.setView(view)
                .setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            PersonaFilterArgs filterArgs = new PersonaFilterArgs();

                            OnFilterListener listener = (OnFilterListener) activity;
                            listener.onFilterSelected(filterArgs);

                            dialog.dismiss();
                        }
                    }
                })
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PersonaFilterArgs filterArgs = new PersonaFilterArgs();

                        selectedArcanaMap = (ArcanaMap) arcanaSpinner.getSelectedItem();
                        filterArgs.arcana = selectedArcanaMap.arcana;
                        filterArgs.rarePersona = rarePersonaCheckBox.isChecked();
                        filterArgs.dlcPersona = dlcPersonaCheckBox.isChecked();

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
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.RED);
            }
        });

        Bundle arguments = getArguments();
        if(arguments != null){
            rarePersonaCheckBox.setChecked(arguments.getBoolean("rarePersona"));
            dlcPersonaCheckBox.setChecked(arguments.getBoolean("dlcPersona"));

            int arcanaOrdinal = arguments.getInt("selectedArcana");

            if(arcanaOrdinal == ANY_ARCANA_ORDINAL){
                this.setArcanaSpinner(null);
            }
            else{
                this.setArcanaSpinner(Enumerations.Arcana.values()[arcanaOrdinal]);
            }

            minLevelEditText.setText(Integer.toString(arguments.getInt("minLevel")));
            maxLevelEditText.setText(Integer.toString(arguments.getInt("maxLevel")));
        }

        return alertDialog;
    }

    private void setArcanaSpinner(Enumerations.Arcana arcanaSelected) {
        if(arcanaSelected == null){
            //if none of the arcana match, set the position to 0 as default
            arcanaSpinner.setSelection(0);
        }

        for(int i = 0; i < arcanaSpinner.getCount(); i++){
            ArcanaMap arcanaMap = (ArcanaMap) arcanaSpinner.getItemAtPosition(i);

            if(arcanaMap.arcana == arcanaSelected){
                arcanaSpinner.setSelection(i);
                return;
            }
        }

        //if none of the arcana match, set the position to 0 as default
        arcanaSpinner.setSelection(0);
    }
}
