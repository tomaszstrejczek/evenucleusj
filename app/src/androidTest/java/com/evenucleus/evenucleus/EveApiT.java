package com.evenucleus.evenucleus;

import android.test.InstrumentationTestCase;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.IEveApiCaller;

import junit.framework.Assert;

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
}
