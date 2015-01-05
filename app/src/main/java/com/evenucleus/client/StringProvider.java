package com.evenucleus.client;

import android.content.Context;

import org.androidannotations.annotations.EBean;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class StringProvider implements IStringProvider {

    private Context _context;
    public StringProvider(Context context)
    {
     _context = context;
    }

    @Override
    public String Get(int id) {
        return _context.getString(id);
    }
}
