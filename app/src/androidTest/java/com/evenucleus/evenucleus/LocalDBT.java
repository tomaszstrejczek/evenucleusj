package com.evenucleus.evenucleus;

import com.evenucleus.client.VersionM;


import java.sql.SQLException;
import java.util.List;
import junit.framework.Assert;

/**
 * Created by tomeks on 2014-12-28.
 */
public class LocalDBT extends TestBase{
    public void test_InsertRecord() throws SQLException {
        List<VersionM> result =_localdb.getVersionDao().queryForAll();
        for(VersionM v :result)
            Assert.assertTrue(!"testver".equals(v.Name));

        VersionM data = new VersionM() {{Name="testver";}};
        _localdb.getVersionDao().createOrUpdate(data);

        result =_localdb.getVersionDao().queryForAll();
        boolean found = false;
        for(VersionM v :result)
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
