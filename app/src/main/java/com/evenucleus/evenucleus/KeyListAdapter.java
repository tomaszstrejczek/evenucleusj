package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evenucleus.client.Corporation;
import com.evenucleus.client.CorporationRepo;
import com.evenucleus.client.ICorporationRepo;
import com.evenucleus.client.IJobRepo;
import com.evenucleus.client.IKeyInfoRepo;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;
import com.evenucleus.client.KeyInfo;
import com.evenucleus.client.KeyInfoRepo;
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
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class KeyListAdapter extends BaseAdapter {
    public class ItemInfo {
        public String Url;
        public String Name;
    }
    public class MyKeyInfo {
        public String Key;
        public int KeyId;
        public List<ItemInfo> Infos;
    }

    List<MyKeyInfo> _myKeyInfos;

    @Bean(KeyInfoRepo.class)
    IKeyInfoRepo _keyInfoRepo;

    @Bean(PilotRepo.class)
    IPilotRepo _pilotRepo;

    @Bean(CorporationRepo.class)
    ICorporationRepo _corporationRepo;

    @RootContext
    Context context;

    @AfterInject
    public void initAdapter() {
        try {
            List<KeyInfo> keyInfos = _keyInfoRepo.GetKeys();
            List<Pilot> pilots = _pilotRepo.GetAll();
            List<Corporation> corpos = _corporationRepo.GetAll();

            _myKeyInfos = new ArrayList<MyKeyInfo>();
            for(KeyInfo k: keyInfos) {
                MyKeyInfo m = new MyKeyInfo();
                m.Key = String.format("ID: %d", k.KeyId);
                m.KeyId = k.KeyId;
                m.Infos = new ArrayList<ItemInfo>();
                for(Pilot p: pilots) {
                    if (p.KeyInfo!=null && p.KeyInfo.KeyId == k.KeyId) {
                        ItemInfo info = new ItemInfo();
                        info.Name = p.Name;
                        info.Url = p.getUrl();
                        m.Infos.add(info);
                    }
                }
                for(Corporation c: corpos) {
                    if (c.KeyInfo!=null && c.KeyInfo.KeyId == k.KeyId) {
                        ItemInfo info = new ItemInfo();
                        info.Name = c.Name;
                        info.Url = c.getUrl();
                        m.Infos.add(info);
                    }
                }

                _myKeyInfos.add(m);
            }
        }
        catch (SQLException e)
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    public void onDeleteKey(int keyid) {
        try {
            _keyInfoRepo.DeleteKey(keyid);
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }

        initAdapter();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _myKeyInfos.size();
    }

    @Override
    public MyKeyInfo getItem(int position) {
        return _myKeyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeyItemView keyItemView;
        if (convertView == null) {
            keyItemView = KeyItemView_.build(context);
        } else {
            keyItemView = (KeyItemView) convertView;
        }

        keyItemView.adapter = this;
        keyItemView.bind(getItem(position));

        return keyItemView;
    }

}
