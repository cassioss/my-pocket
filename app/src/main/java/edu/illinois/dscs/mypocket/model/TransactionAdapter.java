package edu.illinois.dscs.mypocket.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.illinois.dscs.mypocket.R;

/**
 * @author Cassio
 * @version 1.0
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        super(context, R.layout.transaction_row_layout, transactionList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View transactionView = inflater.inflate(R.layout.transaction_row_layout, parent, false);
        Transaction currentTransaction = getItem(position);
        String description = currentTransaction.getDescription();
        String transactionValue = String.format("%.2f", currentTransaction.getValue());

        TextView nameTextView = (TextView) transactionView.findViewById(R.id.transaction_name_text_view);
        nameTextView.setText(description);

        TextView valueTextView = (TextView) transactionView.findViewById(R.id.transaction_value_text_view);
        if (currentTransaction.getType() == 0) {
            valueTextView.setText("-" + valueTextView.getText() + " " + transactionValue);
            valueTextView.setTextColor(Color.RED);
        } else if (currentTransaction.getType() == 1) {
            valueTextView.setText(valueTextView.getText() + " " + transactionValue);
            valueTextView.setTextColor(Color.GREEN);
        }

        return transactionView;
    }
}
