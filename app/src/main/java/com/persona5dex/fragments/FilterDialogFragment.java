package com.persona5dex.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.ArcanaNameProvider.ArcanaName;
import com.persona5dex.R;
import com.persona5dex.ThemeUtil;
import com.persona5dex.activities.BaseActivity;
import com.persona5dex.dagger.fragment.FragmentComponent;
import com.persona5dex.models.ArcanaMap;
import com.persona5dex.models.PersonaFilterArgs;
import com.persona5dex.viewmodels.PersonaFilterViewModel;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class FilterDialogFragment extends DialogFragment {

    private CheckBox rarePersonaCheckBox;
    private CheckBox dlcPersonaCheckBox;
    private EditText minLevelEditText;
    private EditText maxLevelEditText;
    private ArcanaMap selectedArcanaMap;
    private Spinner arcanaSpinner;
    private ArrayAdapter<ArcanaName> arcanaMapArrayAdapter;

    public  interface OnFilterListener {
        void onFilterSelected(PersonaFilterArgs filterArgs);
    }

    @Inject
    PersonaFilterViewModel viewModel;

    private BaseActivity activity;

    public static FilterDialogFragment newInstance(PersonaFilterArgs args){
        //used to restore the fragment's state since the fragment's onRestore and onSave aren't called
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        PersonaFilterArgs filterArgs;
        if(args == null){
            filterArgs = new PersonaFilterArgs();
        }
        else{
            filterArgs = args;
        }

        Bundle bundleArguments = new Bundle();

        bundleArguments.putBoolean("rarePersona", filterArgs.rarePersona);
        bundleArguments.putBoolean("dlcPersona", filterArgs.dlcPersona);
        bundleArguments.putInt("selectedArcana", filterArgs.arcana.value());
        bundleArguments.putInt("minLevel", filterArgs.minLevel);
        bundleArguments.putInt("maxLevel", filterArgs.maxLevel);

        filterDialogFragment.setArguments(bundleArguments);

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

        arcanaSpinner = view.findViewById(R.id.spinner_arcana);

        arcanaMapArrayAdapter = new ArrayAdapter<>(activity,
                R.layout.spinner_dropdown, viewModel.getArcanaNames());
        arcanaSpinner.setAdapter(arcanaMapArrayAdapter);

        InputFilter filter = new InputFilter.LengthFilter(2);

        minLevelEditText = view.findViewById(R.id.editText_minLevel);
        maxLevelEditText = view.findViewById(R.id.editText_maxLevel);

        minLevelEditText.setFilters(new InputFilter[]{filter});
        maxLevelEditText.setFilters(new InputFilter[]{filter});

        rarePersonaCheckBox = view.findViewById(R.id.checkbox_rarePersona);
        dlcPersonaCheckBox = view.findViewById(R.id.checkbox_dlcPersona);

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

                        String arcanaName;
                        if(arcanaSpinner.getSelectedItemPosition() == 0){
                            arcanaName = "";
                        }
                        else{
                            arcanaName = selectedArcanaMap.name.replace("_", " ");
                        }

                        filterArgs.arcana = selectedArcanaMap.arcana;
                        filterArgs.rarePersona = rarePersonaCheckBox.isChecked();
                        filterArgs.dlcPersona = dlcPersonaCheckBox.isChecked();

                        String minLevelText = minLevelEditText.getText().toString();
                        if(!minLevelText.isEmpty()){
                            try{
                                filterArgs.minLevel = Integer.parseInt(minLevelText);
                            }
                            catch (NumberFormatException e){
                                filterArgs.minLevel = 1;
                            }
                        }

                        String maxLevelText = maxLevelEditText.getText().toString();
                        if(!minLevelText.isEmpty()){
                            try{
                                filterArgs.maxLevel = Integer.parseInt(maxLevelText);
                            }
                            catch (NumberFormatException e){
                                filterArgs.maxLevel = 99;
                            }
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

                final int textColorWhite = ThemeUtil.getThemeAttributeColor(requireContext(), R.attr.defaultTextColor);
                final int backgroundColor = ThemeUtil.getThemeAttributeColor(requireContext(), R.attr.pageBackground);

                final Button button = setupButton(textColorWhite, alertDialog, AlertDialog.BUTTON_POSITIVE, backgroundColor);

                final ViewParent parent = button.getParent();
                if(parent instanceof ViewGroup){
                    // hacky, but for some reason background color isn't being applied to parent through themes
                    View parentView = (ViewGroup) parent;
                    parentView.setBackgroundColor(backgroundColor);
                }

                final int colorAccent = ThemeUtil.getThemeAttributeColor(requireContext(), R.attr.colorAccent);
                setupButton(textColorWhite, alertDialog, AlertDialog.BUTTON_NEGATIVE, backgroundColor);
                setupButton(colorAccent, alertDialog, AlertDialog.BUTTON_NEUTRAL, backgroundColor);
            }
        });

        Bundle arguments = getArguments();
        if(arguments != null){
            rarePersonaCheckBox.setChecked(arguments.getBoolean("rarePersona"));
            dlcPersonaCheckBox.setChecked(arguments.getBoolean("dlcPersona"));

            int arcana = arguments.getInt("selectedArcana");
            arcanaSpinner.setSelection(this.getSpinnerPosition(arcana));

            minLevelEditText.setText(Integer.toString(arguments.getInt("minLevel")));
            maxLevelEditText.setText(Integer.toString(arguments.getInt("maxLevel")));
        }

        return alertDialog;
    }

    private Button setupButton(int textColor, AlertDialog alertDialog, int buttonType, int backgroundColor) {
        final Button button = alertDialog.getButton(buttonType);
        button.setTextColor(textColor);
        button.setBackgroundColor(backgroundColor);
        return button;
    }

    private int getSpinnerPosition(int arcana) {

        for (int i = 0; i < arcanaMapArrayAdapter.getCount(); i++) {
            ArcanaName arcanaName = arcanaMapArrayAdapter.getItem(i);

            if(arcanaName.getArcana().value() == arcana){
                return i;
            }
        }

        return 0;
    }
}
