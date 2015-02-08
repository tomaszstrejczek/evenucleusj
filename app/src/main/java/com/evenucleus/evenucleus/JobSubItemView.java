package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.evenucleus.client.Job;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Created by tomeks on 2015-01-12.
 */
@EViewGroup(R.layout.job_sublist_row)
public class JobSubItemView extends LinearLayout {
    @ViewById(R.id.jobName)
    TextView jobName;

    @ViewById(R.id.jobProgress)
    TextView jobProgress;

    @ViewById(R.id.jobImage)
    ImageView jobImage;

    Context _context;

    public JobSubItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(Job job) {
        Ion.with(jobImage)
                .placeholder(R.drawable.person_placeholder_icon)
                .error(R.drawable.noimage)
                .load(job.Url);

        jobName.setText(job.JobDescription);

        // currentTrainingDuration
        DateTime now = new DateTime();
        DateTime end = job.JobEnd==null ? null : new DateTime(job.JobEnd);
        Period per = new Period(now, end, PeriodType.dayTime());
        if (end==null || !end.isAfter(now))
        {
            jobProgress.setText("finished");
            jobProgress.setTextColor(Color.RED);
        }
        else
        {
            jobProgress.setTextColor(Color.parseColor("#10bcc9"));
            if (per.getDays() > 0)
                jobProgress.setText(String.format("%dd %dh %dm", per.getDays(), per.getHours(), per.getMinutes()));
            else
                jobProgress.setText(String.format("%dh %dm", per.getHours(), per.getMinutes()));
        }
    }
}
