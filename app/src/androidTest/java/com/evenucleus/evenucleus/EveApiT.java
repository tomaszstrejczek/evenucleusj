package com.evenucleus.evenucleus;

import android.test.InstrumentationTestCase;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.IEveApiCaller;
import com.evenucleus.client.JournalEntry;

import junit.framework.Assert;

import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
public class EveApiT extends InstrumentationTestCase {

    private IEveApiCaller GetEveApiCaller()
    {
        return new EveApiCaller();
    }


    public void test_SingleChar() throws Exception
    {
        int code = 3483492;
        String vcode = "ZwML01eU6aQUVIEC7gedCEaySiNxRTJxgWo2qoVnxd5duN4tt4CWgMuYMSVNWIUG";
        IEveApiCaller api = GetEveApiCaller();

        Set<EveCharacter> characters = api.getCharacters(code, vcode);
        Assert.assertEquals(1, characters.size());
        Assert.assertEquals("MicioGatto", characters.iterator().next().getName());
    }

    public void test_JournalEntriesCharacters() throws ApiException {
        int code = 3483492;
        String vcode = "ZwML01eU6aQUVIEC7gedCEaySiNxRTJxgWo2qoVnxd5duN4tt4CWgMuYMSVNWIUG";
        IEveApiCaller api = GetEveApiCaller();

        Set<EveCharacter> characters = api.getCharacters(code, vcode);
        Assert.assertEquals(1, characters.size());
        EveCharacter character = characters.iterator().next();
        Assert.assertEquals("MicioGatto", character.getName());

        List<JournalEntry> entries = api.getJournalEntries(code, vcode, character.getCharacterID(), 1, 0);
        Assert.assertTrue(entries.size() > 0);

        // find largest id & check for PilotId
        long largestId = 0;
        for(JournalEntry x:entries)
        {
            if (x.refID > largestId) largestId = x.refID;
            Assert.assertEquals(1, x.PilotId);
        }

        List<JournalEntry> entries2 = api.getJournalEntries(code, vcode, character.getCharacterID(), 1, largestId);
        Assert.assertEquals(0, entries2.size());
    }

    public void test_JournalEntriesCorpo() throws ApiException {
        int code = 3692329;
        String vcode = "aPQOKWEr0r9bp7yVNVgtx9O9xSPDOgTEXY9FhM93ArndOcE3ZTTV1xGnTDHDoeii";
        IEveApiCaller api = GetEveApiCaller();

        List<JournalEntry> entries = api.getJournalEntriesCorpo(code, vcode, 2, 0);
        Assert.assertTrue(entries.size() > 0);

        // find largest id & check for PilotId
        long largestId = 0;
        for(JournalEntry x:entries)
        {
            if (x.refID > largestId) largestId = x.refID;
            Assert.assertEquals(2, x.CorporationId);
        }

        List<JournalEntry> entries2 = api.getJournalEntriesCorpo(code, vcode, 2, largestId);
        Assert.assertEquals(0, entries2.size());
    }

}
