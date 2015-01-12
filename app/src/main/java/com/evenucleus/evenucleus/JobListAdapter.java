package com.evenucleus.evenucleus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.IJobRepo;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class JobListAdapter extends BaseAdapter {
    List<Job> _jobList;
    List<String> _owners;

    @Bean(JobRepo.class)
    IJobRepo _jobRepo;

    @RootContext
    Context context;

    @AfterInject
    public void initAdapter() {
        try {
            _jobList = _jobRepo.GetAll();
            Set<String> owners = new HashSet<String>();
            for(Job j:_jobList) if (!owners.contains(j.Owner)) owners.add(j.Owner);
            _owners = new ArrayList<>(owners);
        }
        catch (SQLException e)
        {
        }
    }

    @Override
    public int getCount() {
        return _owners.size();
    }

    @Override
    public String getItem(int position) {
        return _owners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobItemView jobItemView;
        if (convertView == null) {
            jobItemView = JobItemView_.build(context);
        } else {
            jobItemView = (JobItemView) convertView;
        }

        jobItemView.bind(getItem(position));

        return jobItemView;
    }

}
