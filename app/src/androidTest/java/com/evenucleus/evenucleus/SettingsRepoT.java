package com.evenucleus.evenucleus;

import com.evenucleus.client.SettingsRepo;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by tomeks on 2015-01-18.
 */
public class SettingsRepoT extends TestBase {
    public void test_nextAlert() throws SQLException {
        SettingsRepo repo = new SettingsRepo();
        repo._localdb = _localdb;

        Assert.assertNull(repo.getNextAlert());

        Date value = new Date();
        repo.setNextAlert(value);
        Date value2 = repo.getNextAlert();
        Assert.assertEquals(value, value2);
    }

    public void test_latestAlert() throws SQLException {
        SettingsRepo repo = new SettingsRepo();
        repo._localdb = _localdb;

        Assert.assertNull(repo.getLatestAlert());

        Date value = new Date();
        repo.setLatestAlert(value);
        Date value2 = repo.getLatestAlert();
        Assert.assertEquals(value, value2);
    }
}
