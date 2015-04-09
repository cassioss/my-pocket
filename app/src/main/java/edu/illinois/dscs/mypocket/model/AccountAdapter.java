package edu.illinois.dscs.mypocket.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.R;

/**
 * Created by Dennis on 4/9/15.
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
        String initialValue = String.format("%.2f", currentAccount.getInitialValue());

        TextView nameTextView = (TextView) accountView.findViewById(R.id.account_name_text_view);
        nameTextView.setText(name);

        TextView valueTextView = (TextView) accountView.findViewById(R.id.account_value_text_view);
        valueTextView.setText(valueTextView.getText() + " " + initialValue);

        return accountView;
    }

}
