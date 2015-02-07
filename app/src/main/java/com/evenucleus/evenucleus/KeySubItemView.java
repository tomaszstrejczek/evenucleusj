package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evenucleus.client.Job;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Created by tomeks on 2015-01-12.
 */
@EViewGroup(R.layout.key_sublist_row)
public class KeySubItemView extends LinearLayout {
    @ViewById(R.id.name)
    TextView _name;

    @ViewById(R.id.image)
    ImageView _image;

    Context _context;

    public KeySubItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(KeyListAdapter.ItemInfo info) {
        Ion.with(_image)
                .placeholder(R.drawable.person_placeholder_icon)
                .error(R.drawable.noimage)
                .load(info.Url);

        _name.setText(info.Name);
    }
}
