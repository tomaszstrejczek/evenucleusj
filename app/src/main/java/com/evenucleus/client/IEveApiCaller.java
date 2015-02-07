package com.evenucleus.client;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.character.sheet.CharacterSheetResponse;
import com.beimin.eveapi.character.skill.intraining.SkillInTrainingResponse;
import com.beimin.eveapi.character.skill.queue.SkillQueueResponse;
import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.shared.industryjobs.IndustryJobsResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomeks on 2014-12-28.
 */
public interface IEveApiCaller {
    public boolean CheckKey(int keyid, String vcode) throws ApiException, UserException;
    public boolean IsCorporationKey(int keyid, String vcode) throws ApiException, UserException;
    public Map.Entry<String, Long> GetCorporationData(int keyid, String vcode) throws ApiException, UserException;
    public Set<EveCharacter> getCharacters(int keyid, String vcode) throws ApiException;
    public List<JournalEntry> getJournalEntries(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException;
    public List<JournalEntry> getJournalEntriesCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException;
    public List<WalletTransaction> getWalletTransactions(int keyid, String vcode, long characterId, int pilotid, long lastStoredId) throws ApiException;
    public List<WalletTransaction> getWalletTransactionsCorpo(int keyid, String vcode, int corporationid, long lastStoredId) throws ApiException;
    public CharacterSheetResponse GetCharacterSheet(int keyid, String vcode, long characterId) throws ApiException;
    public SkillInTrainingResponse GetSkillInTraining(int keyid, String vcode, long characterId) throws ApiException;
    public SkillQueueResponse GetSkillQueue(int keyid, String vcode, long characterId) throws ApiException;
    public IndustryJobsResponse getIndustryJobs(int keyid, String vcode, long characterId) throws ApiException;
    public IndustryJobsResponse getIndustryJobsCorpo(int keyid, String vcode) throws ApiException;
}
