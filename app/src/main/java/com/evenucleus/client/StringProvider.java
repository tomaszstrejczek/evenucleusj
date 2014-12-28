package com.evenucleus.client;

import android.content.Context;

/**
 * Created by tomeks on 2014-12-28.
 */
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
