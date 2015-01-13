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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class JobListAdapter extends BaseAdapter {
    public class JobInfo {
        public String Owner;
        public List<Job> Jobs;
    }

    List<JobInfo> _jobInfos;

    @Bean(JobRepo.class)
    IJobRepo _jobRepo;

    @RootContext
    Context context;

    @AfterInject
    public void initAdapter() {
        try {
            List<Job> jobList = _jobRepo.GetAll();
            Map<String, JobInfo> owners = new HashMap<String, JobInfo>();
            for(Job j:jobList)
                if (owners.containsKey(j.Owner))
                    owners.get(j.Owner).Jobs.add(j);
                else
                {
                    JobInfo ji = new JobInfo();
                    ji.Owner = j.Owner;
                    ji.Jobs = new ArrayList(Arrays.asList(j));
                    owners.put(ji.Owner, ji);
                }

            _jobInfos = new ArrayList<JobInfo>(owners.values());
        }
        catch (SQLException e)
        {
        }
    }

    @Override
    public int getCount() {
        return _jobInfos.size();
    }

    @Override
    public JobInfo getItem(int position) {
        return _jobInfos.get(position);
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
