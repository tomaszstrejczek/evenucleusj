package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.Number2RoundedString;
import com.evenucleus.client.Pilot;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;

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
    @ViewById(R.id.checkbox)
    public CheckBox checkBox;

    EnrichedJournalEntry _journalEntry;

    Context _context;

    public JournalItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(EnrichedJournalEntry entry) {
        _journalEntry = entry;
        date.setText(DateFormat.getDateTimeInstance().format(entry.Date));
        description.setText(entry.Description);
        SetCategory(entry.Category);
        amount.setText(Number2RoundedString.Convert(entry.Amount));
        checkBox.setChecked(entry.Selected);
    }

    @CheckedChange(R.id.checkbox)
    void checkedChangedOnMyButton(boolean isChecked, CompoundButton button) {
        _journalEntry.Selected = isChecked;
    }

    public void SetCategory(String cat) {
        if (cat != null)
            category.setText(cat);
        else
            category.setText("");

    }
}
