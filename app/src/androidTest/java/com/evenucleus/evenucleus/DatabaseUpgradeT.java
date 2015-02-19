package com.evenucleus.evenucleus;

import android.test.InstrumentationTestCase;

import com.evenucleus.client.DatabaseHelper;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.WalletTransaction;

import junit.framework.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2015-02-19.
 */
public class DatabaseUpgradeT extends InstrumentationTestCase {
    public void test_Version1() throws IOException, SQLException {
        File databasePath = File.createTempFile("evenucleus", "db", getInstrumentation().getContext().getFilesDir());
        InputStream stream = getInstrumentation().getContext().getResources().getAssets().open("evenucleus1.db");
        createFileFromInputStream(stream, databasePath);

        DatabaseHelper db = new DatabaseHelper();
        db.Initialize(databasePath.getPath());

        long cnt = db.getJournalEntryDao().countOf();
        Assert.assertTrue(cnt>0);

        cnt = db.getWalletTransactionDao().countOf();
        Assert.assertTrue(cnt>0);

        List<JournalEntry> jes = db.getJournalEntryDao().queryForAll();
        Assert.assertTrue(jes.size()>0);

        List<WalletTransaction> wts = db.getWalletTransactionDao().queryForAll();
        Assert.assertTrue(wts.size()>0);
    }

    private void createFileFromInputStream(InputStream inputStream, File f) throws IOException {
        OutputStream outputStream = new FileOutputStream(f);
        byte buffer[] = new byte[16*1024];
        int length = 0;

        while((length=inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }

        outputStream.close();
        inputStream.close();
    }
}
