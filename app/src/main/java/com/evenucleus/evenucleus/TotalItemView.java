package com.evenucleus.evenucleus;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.ITotalsCalculator;
import com.evenucleus.client.Number2RoundedString;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

/**
 * Created by tomeks on 2015-01-04.
 */
@EViewGroup(R.layout.total_list_row)
public class TotalItemView extends LinearLayout {
    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.income)
    TextView income;
    @ViewById(R.id.expense)
    TextView expense;
    @ViewById(R.id.profit)
    TextView profit;

    Context _context;

    public TotalItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(ITotalsCalculator.Total entry) {
        name.setText(entry.Name);
        income.setText(Number2RoundedString.Convert(entry.Income));
        expense.setText(Number2RoundedString.Convert(entry.Expense));
        profit.setText(Number2RoundedString.Convert(entry.Profit));
    }
}
