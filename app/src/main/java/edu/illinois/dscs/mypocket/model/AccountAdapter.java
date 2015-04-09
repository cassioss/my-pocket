package edu.illinois.dscs.mypocket.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.R;

/**
 * @author Dennis
 * @version 1.0
 */
public class AccountAdapter extends ArrayAdapter<Account> {


    public AccountAdapter(Context context, ArrayList<Account> AccountList) {
        super(context, R.layout.show_account_row_layout, AccountList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater accountInflater = LayoutInflater.from(getContext());
        View accountView = accountInflater.inflate(R.layout.show_account_row_layout, parent, false);

        Account currentAccount = getItem(position);

        String name = currentAccount.getName();
        double valueDouble = currentAccount.getInitialValue();
        String initialValue = String.format("%.2f", Math.abs(valueDouble));

        TextView nameTextView = (TextView) accountView.findViewById(R.id.account_name_text_view);
        nameTextView.setText(name);

        TextView valueTextView = (TextView) accountView.findViewById(R.id.account_value_text_view);

        if (valueDouble < 0.00) {
            valueTextView.setText("-" + valueTextView.getText() + " " + initialValue);
            valueTextView.setTextColor(Color.RED);
        } else {
            valueTextView.setText(valueTextView.getText() + " " + initialValue);
            if (valueDouble > 0.00)
                valueTextView.setTextColor(Color.GREEN);
            else
                valueTextView.setTextColor(Color.BLACK);
        }

        return accountView;
    }

}
