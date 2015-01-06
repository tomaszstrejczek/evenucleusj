package com.evenucleus.evenucleus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.mocks.PilotRepoMock;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2015-01-04.
 */
@EBean
public class PilotListAdapter extends BaseAdapter{

    List<Pilot> _pilotList;

    @Bean(PilotRepo.class)
    IPilotRepo _pilotRepo;

    @RootContext
    Context context;

    @AfterInject
    public void initAdapter() {
        try {
            _pilotList = _pilotRepo.GetAll();
        }
        catch (SQLException e)
        {
        }
    }

    @Override
    public int getCount() {
        return _pilotList.size();
    }

    @Override
    public Pilot getItem(int position) {
        return _pilotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PilotItemView pilotItemView;
        if (convertView == null) {
            pilotItemView = PilotItemView_.build(context);
        } else {
            pilotItemView = (PilotItemView) convertView;
        }

        pilotItemView.bind(getItem(position));

        return pilotItemView;
    }
}
