package edu.illinois.dscs.mypocket.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Private class that uses a TextWatcher specifically for currency. Format $#.##
 *
 * @author Cassio
 * @version 1.1
 * @since 1.0
 */
public class CurrencyTextWatcher implements TextWatcher {

    private String current = "";
    private EditText value;

    public CurrencyTextWatcher(EditText value) {
        this.value = value;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            value.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[$,.]", "");

            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format(parsed / 100);

            current = formatted;
            value.setText(formatted);
            value.setSelection(formatted.length());

            value.addTextChangedListener(this);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}