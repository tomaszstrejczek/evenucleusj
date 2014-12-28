package com.evenucleus.evenucleus;

import com.evenucleus.client.Version;


import java.sql.SQLException;
import java.util.List;
import junit.framework.Assert;

/**
 * Created by tomeks on 2014-12-28.
 */
public class LocalDBT extends TestBase{
    public void test_InsertRecord() throws SQLException {
        List<Version> result =_localdb.getDao().queryForAll();
        for(Version v :result)
            Assert.assertTrue(!"testver".equals(v.Name));

        Version data = new Version() {{Name="testver";}};
        _localdb.getDao().createOrUpdate(data);

        result =_localdb.getDao().queryForAll();
        boolean found = false;
        for(Version v :result)
            if (v.Name.equals("testver"))
                found = true;

        Assert.assertTrue(found);
    }

    public void test_InsertRecord2() throws SQLException {
        test_InsertRecord();
    }

    public void test_InsertRecord3() throws SQLException {
        test_InsertRecord();
    }
}
