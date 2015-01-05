package com.evenucleus.evenucleus;

import com.evenucleus.client.KeyInfo;
import com.evenucleus.client.KeyInfoRepo;
import com.evenucleus.client.StringProvider;
import com.evenucleus.client.UserException;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-28.
 */
public class KeyRepoT extends TestBase {
    public void test_AddKey() throws SQLException, UserException {
        KeyInfoRepo repo = new KeyInfoRepo();
        repo._localdb = _localdb;
        repo._stringProvider = new StringProvider(getContext());

        repo.AddKey(1, "ala");
        List<KeyInfo> result = repo.GetKeys();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ala", result.iterator().next().VCode);

        repo.AddKey(2, "kot");
        result = repo.GetKeys();
        Assert.assertEquals(2, result.size());
        KeyInfo first = null, second = null;
        for(KeyInfo k:result)
            if (k.KeyId==1)
                first = k;
            else
                second = k;

        Assert.assertEquals("ala", first.VCode);
        Assert.assertEquals("kot", second.VCode);

        repo.DeleteKey(1);
        result = repo.GetKeys();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("kot", result.iterator().next().VCode);

        repo.DeleteKey(2);
        result = repo.GetKeys();
        Assert.assertEquals(0, result.size());
    }

}
