package com.example.rechee.persona5calculator.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.activities.BaseActivity;
import com.example.rechee.persona5calculator.dagger.FragmentComponent;
import com.example.rechee.persona5calculator.viewmodels.PersonaFilterViewModel;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class FilterDialogFragment extends DialogFragment {

    @Inject
    PersonaFilterViewModel viewModel;

    private BaseActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.activity = (BaseActivity) getActivity();
        FragmentComponent component = activity.getComponent().plus();
        component.inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.filter_dialog, null);

        Spinner arcanaSpinner = (Spinner) view.findViewById(R.id.spinner_arcana);
        //builder.setView()
        return new Dialog(getContext());
    }
}
