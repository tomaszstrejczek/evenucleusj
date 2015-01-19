package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.Pilot;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Created by tomeks on 2015-01-04.
 */
@EViewGroup(R.layout.journal_list_row)
public class JournalItemView extends LinearLayout {
    @ViewById(R.id.date)
    TextView date;
    @ViewById(R.id.description)
    TextView description;
    @ViewById(R.id.category)
    TextView category;
    @ViewById(R.id.amount)
    TextView amount;

    Context _context;

    public JournalItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(EnrichedJournalEntry entry) {
        date.setText(entry.Date.toString());
        description.setText(entry.Description);
        if (entry.Category != null)
            category.setText(entry.Category);
        else
            category.setText("");
        amount.setText(Double.toString(entry.Amount));
    }
}
