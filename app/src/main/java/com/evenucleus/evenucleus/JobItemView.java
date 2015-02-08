package com.evenucleus.evenucleus;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.evenucleus.client.Job;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tomeks on 2015-01-12.
 */
@EViewGroup(R.layout.job_list_row)
public class JobItemView extends LinearLayout {
    @ViewById
    TextView pilotName;

    @ViewById(R.id.jobsSublist)
    LinearLayout jobsSublist;

    Context _context;

    public JobItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(JobListAdapter.JobInfo info) {
        pilotName.setText(info.Owner);

        jobsSublist.removeAllViews();
        for(Job j: info.Jobs) {
            JobSubItemView view = JobSubItemView_.build(getContext());
            view.bind(j);
            jobsSublist.addView(view);
        }
    }
}
