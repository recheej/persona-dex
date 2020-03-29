package com.persona5dex.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.persona5dex.ArcanaNameProvider.ArcanaName;
import com.persona5dex.Persona5Application;
import com.persona5dex.R;
import com.persona5dex.activities.BaseActivity;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.viewmodels.PersonaMainListViewModel;
import com.persona5dex.viewmodels.ViewModelFactory;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class FilterDialogFragment extends DialogFragment {

    private CheckBox basePersonasCheckbox;
    private CheckBox rarePersonasCheckbox;
    private CheckBox dlcPersonaCheckBox;
    private EditText minLevelEditText;
    private EditText maxLevelEditText;
    private ArcanaName selectedArcanaName;
    private Spinner arcanaSpinner;
    private ArrayAdapter<ArcanaName> arcanaMapArrayAdapter;
    private PersonaMainListViewModel personaMainListViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    public static FilterDialogFragment newInstance() {
        return new FilterDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BaseActivity activity = (BaseActivity) getActivity();

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(requireActivity());
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.filter_dialog, null);

        Persona5Application.get(requireActivity()).getComponent()
                .activityComponent()
                .activityContext(requireActivity())
                .build().inject(this);

        arcanaSpinner = view.findViewById(R.id.spinner_arcana);

        InputFilter filter = new InputFilter.LengthFilter(2);

        minLevelEditText = view.findViewById(R.id.editText_minLevel);
        maxLevelEditText = view.findViewById(R.id.editText_maxLevel);

        minLevelEditText.setFilters(new InputFilter[]{filter});
        maxLevelEditText.setFilters(new InputFilter[]{filter});

        basePersonasCheckbox = view.findViewById(R.id.checkbox_base_personas);
        rarePersonasCheckbox = view.findViewById(R.id.checkbox_royal_personas);
        dlcPersonaCheckBox = view.findViewById(R.id.checkbox_dlcPersona);

        builder.setView(view)
                .setNeutralButton(R.string.reset, (dialog, which) -> {
                    if(dialog != null) {
                        personaMainListViewModel.filterPersonas(new PersonaFilterArgs());
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.filter, (dialog, which) -> {
                    filter();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personaMainListViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(PersonaMainListViewModel.class);

        arcanaMapArrayAdapter = new ArrayAdapter<>(requireActivity(),
                R.layout.spinner_dropdown, personaMainListViewModel.getArcanaNamesForSpinner());
        arcanaSpinner.setAdapter(arcanaMapArrayAdapter);

        personaMainListViewModel.getFilterArgs().observe(requireActivity(), personaFilterArgs -> {
            basePersonasCheckbox.setChecked(personaFilterArgs.basePersonas);
            rarePersonasCheckbox.setChecked(personaFilterArgs.royalPersonas);
            dlcPersonaCheckBox.setChecked(personaFilterArgs.dlcPersona);

            arcanaSpinner.setSelection(this.getSpinnerPosition(personaFilterArgs.arcana));

            minLevelEditText.setText(String.format(Locale.ROOT, "%d", personaFilterArgs.minLevel));
            maxLevelEditText.setText(String.format(Locale.ROOT, "%d", personaFilterArgs.maxLevel));
        });
    }

    private void filter() {
        PersonaFilterArgs filterArgs = new PersonaFilterArgs();

        selectedArcanaName = (ArcanaName) arcanaSpinner.getSelectedItem();

        filterArgs.arcana = selectedArcanaName.getArcana();
        filterArgs.basePersonas = basePersonasCheckbox.isChecked();
        filterArgs.royalPersonas = rarePersonasCheckbox.isChecked();
        filterArgs.dlcPersona = dlcPersonaCheckBox.isChecked();

        String minLevelText = minLevelEditText.getText().toString();
        if(!minLevelText.isEmpty()) {
            try {
                filterArgs.minLevel = Integer.parseInt(minLevelText);
            } catch(NumberFormatException e) {
                filterArgs.minLevel = 1;
            }
        }

        String maxLevelText = maxLevelEditText.getText().toString();
        if(!minLevelText.isEmpty()) {
            try {
                filterArgs.maxLevel = Integer.parseInt(maxLevelText);
            } catch(NumberFormatException e) {
                filterArgs.maxLevel = 99;
            }
        }

        personaMainListViewModel.filterPersonas(filterArgs);
    }

    private int getSpinnerPosition(Enumerations.Arcana arcana) {

        for(int i = 0; i < arcanaMapArrayAdapter.getCount(); i++) {
            ArcanaName arcanaName = arcanaMapArrayAdapter.getItem(i);

            assert arcanaName != null;
            if(arcanaName.getArcana() == arcana) {
                return i;
            }
        }

        return 0;
    }
}
