package com.evenucleus.evenucleus;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tomeks on 2015-01-12.
 */
@EViewGroup(R.layout.job_list_row)
public class JobItemView extends LinearLayout {
    @ViewById
    TextView pilotName;

    Context _context;

    public JobItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(String name) {
        pilotName.setText(name);
    }
}
