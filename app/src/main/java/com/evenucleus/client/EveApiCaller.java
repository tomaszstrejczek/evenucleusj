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
import com.beimin.eveapi.shared.industryjobs.IndustryJobsResponse;
import com.beimin.eveapi.shared.wallet.journal.ApiJournalEntry;
import com.beimin.eveapi.shared.wallet.journal.WalletJournalResponse;
import com.beimin.eveapi.shared.wallet.transactions.ApiWalletTransaction;
import com.beimin.eveapi.shared.wallet.transactions.WalletTransactionsResponse;

import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
@EBean
public class EveApiCaller implements IEveApiCaller {
    final Logger logger = LoggerFactory.getLogger(EveApiCaller.class);

    @Override
    public boolean CheckKey(int keyid, String vcode) throws ApiException, UserException {
        logger.debug("CheckKey {}", keyid);
        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        boolean result = response.isCharacterKey() || response.isCorporationKey() || response.isAccountKey();
        logger.debug("CheckKey result {}", result);
        return result;
    }

    @Override
    public boolean IsCorporationKey(int keyid, String vcode) throws ApiException, UserException {
        logger.debug("IsCorporationKey {}", keyid);

        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        boolean result = response.isCorporationKey();
        logger.debug("IsCorporationKey result {}", result);
        return result;
    }

    @Override
    public Map.Entry<String, Long> GetCorporationData(int keyid, String vcode) throws ApiException, UserException {
        logger.debug("GetCorporationData {}", keyid);

        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        ApiKeyInfoResponse response = api.getAPIKeyInfo();
        if (response.hasError())
            throw new UserException(response.getError().toString());
        Map.Entry<String, Long> result = new AbstractMap.SimpleEntry<String, Long>(response.getEveCharacters().iterator().next().getCorporationName(), response.getEveCharacters().iterator().next().getCorporationID());
        logger.debug("GetCorporationData result {} {}", result.getKey(), result.getValue());
        return result;
    }

    public Set<EveCharacter> getCharacters(int keyid, String vcode) throws ApiException {
        logger.debug("getCharacters {}", keyid);

        EveApi api = new EveApi();
        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        api.setAuth(auth);
        Set<EveCharacter> result = api.getCharacters();

        StringBuilder builder = new StringBuilder();
        for(EveCharacter ev: result) {
            builder.append(ev.getName());
            builder.append('-');
            builder.append(ev.getCharacterID());
            builder.append(';');
        }
        logger.debug("getCharacters result {}", builder.toString());
        return result;
    }

    @Override
    public List<JournalEntry> getJournalEntries(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException {
        logger.debug("getJournalEntries {} {} {}", keyid, characterId, lastStoredId);
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
            entry.RefTypeName = x.getRefType()==null?String.format("%d",x.getRefID()):x.getRefType().getName();
            result.add(entry);
        }

        logger.debug("getJournalEntries returns count {}", result.size());
        return result;
    }

    @Override
    public List<JournalEntry> getJournalEntriesCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException {
        logger.debug("getJournalEntriesCorpo {} {}", keyid, lastStoredId);

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

        logger.debug("getJournalEntriesCorpo returns count {}", result.size());
        return result;
    }

    @Override
    public List<WalletTransaction> getWalletTransactions(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException {
        logger.debug("getWalletTransactions keyid={} characterid={} laststoredid={}", keyid, characterId, lastStoredId);

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

        logger.debug("getWalletTransactions returns count {}", result.size());
        return result;
    }

    @Override
    public List<WalletTransaction> getWalletTransactionsCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException {
        logger.debug("getWalletTransactionsCorpo keyud={} corporationid={} laststoredid={}", keyid, corporationid, lastStoredId);

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

        logger.debug("getWalletTransactionsCorpo returns count {}", result.size());
        return result;
    }

    @Override
    public CharacterSheetResponse GetCharacterSheet(int keyid, String vcode, long characterId) throws ApiException {
        logger.debug("GetCharacterSheet keyid={} characterid={}", keyid, characterId);

        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        return CharacterSheetParser.getInstance().getResponse(auth);
    }

    @Override
    public SkillInTrainingResponse GetSkillInTraining(int keyid, String vcode, long characterId) throws ApiException {
        logger.debug("GetSkillInTraining keyid={} characterid={}", keyid, characterId);

        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        return SkillInTrainingParser.getInstance().getResponse(auth);
    }

    @Override
    public SkillQueueResponse GetSkillQueue(int keyid, String vcode, long characterId) throws ApiException {
        logger.debug("GetSkillQueue keyid={} characterid={} ", keyid, characterId);

        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);
        SkillQueueResponse result= SkillQueueParser.getInstance().getResponse(auth);
        logger.debug("GetSkillQueue result count {}", result.getAll().size());
        return result;
    }

    @Override
    public IndustryJobsResponse getIndustryJobs(int keyid, String vcode, long characterId) throws ApiException {
        logger.debug("getIndustryJobs keyid={} characterid={}", keyid, characterId);

        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        auth.setCharacterID(characterId);

        IndustryJobsResponse result = com.beimin.eveapi.character.industryjobs.IndustryJobsParser.getInstance().getResponse(auth);
        logger.debug("getIndustryJobs result count {}", result.getAll().size());
        return result;
    }

    @Override
    public IndustryJobsResponse getIndustryJobsCorpo(int keyid, String vcode) throws ApiException {
        logger.debug("getIndustryJobsCorpo keyid={}", keyid);

        ApiAuthorization auth = new ApiAuthorization(keyid, vcode);
        IndustryJobsResponse result = com.beimin.eveapi.corporation.industryjobs.IndustryJobsParser.getInstance().getResponse(auth);
        logger.debug("getIndustryJobsCorpo result count {}", result.getAll().size());
        return result;
    }

}
