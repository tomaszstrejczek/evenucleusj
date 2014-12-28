package com.evenucleus.client;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.exception.ApiException;

import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
public interface IEveApiCaller {
    public Set<EveCharacter> getCharacters(int keyid, String vcode) throws ApiException;
}
