package com.evenucleus.evenucleus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.IJobRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class JobSublistAdapter extends BaseAdapter {
    @RootContext
    Context context;

    public JobListAdapter.JobInfo _jobInfo;

    @Override
    public int getCount() {
        return _jobInfo.Jobs.size();
    }

    @Override
    public Job getItem(int position) {
        return _jobInfo.Jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobSubItemView jobItemView;
        if (convertView == null) {
            jobItemView = JobSubItemView_.build(context);
        } else {
            jobItemView = (JobSubItemView) convertView;
        }

        jobItemView.bind(getItem(position));

        return jobItemView;
    }

}
