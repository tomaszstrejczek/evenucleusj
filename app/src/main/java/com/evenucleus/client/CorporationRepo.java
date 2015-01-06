package com.evenucleus.client;

import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class CorporationRepo implements ICorporationRepo {
    @Bean
    public DatabaseHelper _localdb;

    @Override
    public void Update(UserData data) throws SQLException {
        Log.d(CorporationRepo.class.getName(), "Update");

        List<String> validCorpoNames = new ArrayList<String>();
        for(Corporation s:data.Corporations)
            validCorpoNames.add(s.Name);

        List<Corporation> storedCorpos = _localdb.getCorporationDao().queryForAll();
        List<Corporation> toremove = new ArrayList<>();
        for(Corporation s:storedCorpos)
            if (!validCorpoNames.contains(s.Name))
                toremove.add(s);

        for(Corporation r:toremove)
        {
            Log.d(CorporationRepo.class.getName(), String.format("removing %s", r.Name));
            _localdb.getCorporationDao().deleteById(r.CorporationId);
        }

        List<String> storedCorporNames = new ArrayList<String>();
        for(Corporation s:storedCorpos) storedCorporNames.add(s.Name);
        List<Corporation> toadd = new ArrayList<Corporation>();
        for(Corporation s: data.Corporations)
            if (!storedCorporNames.contains(s.Name))
                toadd.add(s);
        for (Corporation a:toadd)
        {
            Log.d(CorporationRepo.class.getName(), String.format("adding %s", a.Name));
            _localdb.getCorporationDao().createOrUpdate(a);
        }
    }

    @Override
    public List<Corporation> GetAll() throws SQLException {
        Log.d(CorporationRepo.class.getName(), "GetAll");
        return _localdb.getCorporationDao().queryForAll();
    }
}
