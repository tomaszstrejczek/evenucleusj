package com.evenucleus.evenucleus;

import android.content.Context;
import android.util.Log;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.shared.wallet.journal.ApiJournalEntry;
import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.IEveApiCaller;
import com.evenucleus.client.JournalEnricher;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.WalletTransaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2015-01-01.
 */
public class JournalEnricherT extends TestBase {
    private IEveApiCaller GetEveApiCaller()
    {
        return new EveApiCaller();
    }

    private List<JournalEntry> getJes(int code, String vcode) throws ApiException {
        IEveApiCaller api = GetEveApiCaller();

        Set<EveCharacter> characters = api.getCharacters(code, vcode);
        Assert.assertEquals(1, characters.size());
        EveCharacter character = characters.iterator().next();

        List<JournalEntry> entries = api.getJournalEntries(code, vcode, character.getCharacterID(), 2, 0);
        return entries;
    }

    private List<WalletTransaction> getWts(int code, String vcode) throws ApiException {
        IEveApiCaller api = GetEveApiCaller();

        Set<EveCharacter> characters = api.getCharacters(code, vcode);
        Assert.assertEquals(1, characters.size());
        EveCharacter character = characters.iterator().next();

        List<WalletTransaction> entries = api.getWalletTransactions(code, vcode, character.getCharacterID(), 2, 0);
        return entries;
    }

    public void test_MicioGatto() throws ApiException {
        JournalEnricher enricher = new JournalEnricher();
        int code = 3483492;
        String vcode = "ZwML01eU6aQUVIEC7gedCEaySiNxRTJxgWo2qoVnxd5duN4tt4CWgMuYMSVNWIUG";
        List<JournalEntry> jes = getJes(code, vcode);
        List<WalletTransaction> wts = getWts(code, vcode);

        List<EnrichedJournalEntry> result = enricher.Enrich(jes, wts);

        double srcsum = 0;
        for(JournalEntry j:jes) srcsum += j.amount;
        double destsum = 0;
        for(EnrichedJournalEntry j:result) destsum += j.Amount;

        Assert.assertTrue(Math.abs(srcsum - destsum) < 10000.0);
    }

    public void test_tomek2() throws ApiException {
        JournalEnricher enricher = new JournalEnricher();
        int code = 3996593;
        String vcode = "D3bKyktDGxpgR32WjxDPr72jty6URzn6yTK6FzPGA7r0CgKIc6eKwE6PhUYscxNv";
        List<JournalEntry> jes = getJes(code, vcode);
        List<WalletTransaction> wts = getWts(code, vcode);

        List<EnrichedJournalEntry> result = enricher.Enrich(jes, wts);

        double srcsum = 0;
        for(JournalEntry j:jes) srcsum += j.amount;
        double destsum = 0;
        for(EnrichedJournalEntry j:result) destsum += j.Amount;

        Assert.assertTrue(Math.abs(srcsum - destsum) < 10000.0);
    }

}
