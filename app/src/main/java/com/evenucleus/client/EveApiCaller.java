package com.evenucleus.client;

import com.beimin.eveapi.EveApi;
import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.core.ApiAuthorization;
import com.beimin.eveapi.exception.ApiException;

import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
public class EveApiCaller implements IEveApiCaller{
    public Set<EveCharacter> getCharacters(int keyid, String vcode) throws ApiException {
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        return api.getCharacters();
    }
}
