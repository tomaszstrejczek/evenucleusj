package com.evenucleus.client;

import com.beimin.eveapi.EveApi;
import com.beimin.eveapi.account.apikeyinfo.ApiKeyInfoResponse;
import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.character.sheet.CharacterSheetParser;
import com.beimin.eveapi.character.sheet.CharacterSheetResponse;
import com.beimin.eveapi.character.skill.intraining.SkillInTrainingParser;
import com.beimin.eveapi.character.skill.intraining.SkillInTrainingResponse;
import com.beimin.eveapi.character.skill.queue.SkillQueueParser;
import com.beimin.eveapi.character.skill.queue.SkillQueueResponse;
import com.beimin.eveapi.core.ApiAuthorization;
import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.shared.wallet.journal.ApiJournalEntry;
import com.beimin.eveapi.shared.wallet.journal.WalletJournalResponse;
import com.beimin.eveapi.shared.wallet.transactions.ApiWalletTransaction;
import com.beimin.eveapi.shared.wallet.transactions.WalletTransactionsResponse;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class EveApiCaller implements IEveApiCaller {
    @Override
    public boolean CheckKey(int keyid, String vcode) throws ApiException, UserException {
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        return response.isCharacterKey() || response.isCorporationKey() || response.isAccountKey();
    }

    @Override
    public boolean IsCorporationKey(int keyid, String vcode) throws ApiException, UserException {
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        return response.isCorporationKey();
    }

    @Override
    public String GetCorporationName(int keyid, String vcode) throws ApiException, UserException {
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        return response.getEveCharacters().iterator().next().getCorporationName();
    }

    public Set<EveCharacter> getCharacters(int keyid, String vcode) throws ApiException {
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        return api.getCharacters();
    }

    @Override
    public List<JournalEntry> getJournalEntries(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);

        List<ApiJournalEntry> result1 = new ArrayList<ApiJournalEntry>();
        long fromId = 0;
        for(;;)
        {
            WalletJournalResponse response = com.beimin.eveapi.character.wallet.journal.WalletJournalParser.getInstance().getWalletJournalResponse(auth, fromId);
            Set<ApiJournalEntry> entries = response.getAll();
            if (entries.size()==0)
                break;

            for(ApiJournalEntry x:entries)
            {
                if (x.getRefID() > lastStoredId)
                    result1.add(x);

                if (fromId==0 || x.getRefID() < fromId)
                    fromId = x.getRefID();
            }

        }

        List<JournalEntry> result = new ArrayList<JournalEntry>();
        for(ApiJournalEntry x:result1)
        {
            JournalEntry entry = new JournalEntry(x);
            entry.PilotId = pilotid;
            entry.RefTypeName = x.getRefType().getName();
            result.add(entry);
        }

        return result;
    }

    @Override
    public List<JournalEntry> getJournalEntriesCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);

        List<ApiJournalEntry> result1 = new ArrayList<ApiJournalEntry>();
        long fromId = 0;
        for(;;)
        {
            WalletJournalResponse response = com.beimin.eveapi.corporation.wallet.journal.WalletJournalParser.getInstance().getResponse(auth, 1000, fromId, 1000);
            Set<ApiJournalEntry> entries = response.getAll();
            if (entries.size()==0)
                break;

            for(ApiJournalEntry x:entries)
            {
                if (x.getRefID() > lastStoredId)
                    result1.add(x);

                if (fromId==0 || x.getRefID() < fromId)
                    fromId = x.getRefID();
            }

        }

        List<JournalEntry> result = new ArrayList<JournalEntry>();
        for(ApiJournalEntry x:result1)
        {
            JournalEntry entry = new JournalEntry(x);
            entry.CorporationId= corporationid;
            entry.RefTypeName = x.getRefType()==null? "<unknown>": x.getRefType().getName();
            result.add(entry);
        }

        return result;
    }

    @Override
    public List<WalletTransaction> getWalletTransactions(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);

        List<ApiWalletTransaction> result1 = new ArrayList<ApiWalletTransaction>();
        long fromId = 0;
        for(;;)
        {
            WalletTransactionsResponse response = com.beimin.eveapi.character.wallet.transactions.WalletTransactionsParser.getInstance().getTransactionsResponse(auth, fromId);
            Set<ApiWalletTransaction> entries = response.getAll();
            if (entries.size()==0)
                break;

            for(ApiWalletTransaction x:entries)
            {
                if (x.getTransactionID() > lastStoredId)
                    result1.add(x);

                if (fromId==0 || x.getTransactionID() < fromId)
                    fromId = x.getTransactionID();
            }

        }

        List<WalletTransaction> result = new ArrayList<WalletTransaction>();
        for(ApiWalletTransaction x:result1)
        {
            WalletTransaction entry = new WalletTransaction(x);
            entry.PilotId = pilotid;
            result.add(entry);
        }

        return result;
    }

    @Override
    public List<WalletTransaction> getWalletTransactionsCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);

        List<ApiWalletTransaction> result1 = new ArrayList<ApiWalletTransaction>();
        long fromId = 0;
        for(;;)
        {
            WalletTransactionsResponse response = com.beimin.eveapi.corporation.wallet.transactions.WalletTransactionsParser.getInstance().getResponse(auth, 1000, fromId, 1000);
            Set<ApiWalletTransaction> entries = response.getAll();
            if (entries.size()==0)
                break;

            for(ApiWalletTransaction x:entries)
            {
                if (x.getTransactionID() > lastStoredId)
                    result1.add(x);

                if (fromId==0 || x.getTransactionID() < fromId)
                    fromId = x.getTransactionID();
            }

        }

        List<WalletTransaction> result = new ArrayList<WalletTransaction>();
        for(ApiWalletTransaction x:result1)
        {
            WalletTransaction entry = new WalletTransaction(x);
            entry.CorporationId =corporationid;
            result.add(entry);
        }

        return result;
    }

    @Override
    public CharacterSheetResponse GetCharacterSheet(int keyid, String vcode, long characterId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        return CharacterSheetParser.getInstance().getResponse(auth);
    }

    @Override
    public SkillInTrainingResponse GetSkillInTraining(int keyid, String vcode, long characterId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        return SkillInTrainingParser.getInstance().getResponse(auth);
    }

    @Override
    public SkillQueueResponse GetSkillQueue(int keyid, String vcode, long characterId) throws ApiException {
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        return SkillQueueParser.getInstance().getResponse(auth);
    }

}
