package com.evenucleus.client;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.beimin.eveapi.account.characters.EveCharacter;
import com.beimin.eveapi.character.sheet.ApiSkill;
import com.beimin.eveapi.character.sheet.CharacterSheetResponse;
import com.beimin.eveapi.character.skill.intraining.SkillInTrainingResponse;
import com.beimin.eveapi.character.skill.queue.ApiSkillQueueItem;
import com.beimin.eveapi.character.skill.queue.SkillQueueResponse;
import com.beimin.eveapi.exception.ApiException;


/**
 * Created by tomeks on 2015-01-06.
 */
@EBean
public class PilotService implements IPilotService {
    final Logger logger = LoggerFactory.getLogger(PilotService.class);

    @Bean(KeyInfoRepo.class)
    public IKeyInfoRepo _keyInfoRepo;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Bean(TypeNameDict.class)
    public ITypeNameDict _typeNameDict;

    @Bean(SettingsRepo.class)
    public ISettingsRepo _settingsRepo;

    @Override
    public Result Get() throws SQLException, UserException, ApiException {
        logger.debug("Get");

        List<DateTime> cachedUntils = new ArrayList<DateTime>();

        Result r = new Result();
        r.pilots = new ArrayList<PilotDTO>();
        r.corporations = new ArrayList<Corporation>();
        r.cachedUntil = new DateTime().withFieldAdded(DurationFieldType.hours(), 1);

        List<KeyInfo> keys = _keyInfoRepo.GetKeys();
        for(KeyInfo k:keys) {
            if (_eveApiCaller.IsCorporationKey(k.KeyId, k.VCode))
            {
                Corporation ci = new Corporation();
                ci.KeyInfo = k;
                Map.Entry<String, Long> v = _eveApiCaller.GetCorporationData(k.KeyId, k.VCode);
                ci.Name = v.getKey();
                ci.EveCorporationId = v.getValue();
                r.corporations.add(ci);
                continue;
            }

            Set<EveCharacter> characters = _eveApiCaller.getCharacters(k.KeyId, k.VCode);
            for(EveCharacter c:characters) {
                // Skip pilots already handled
                boolean found = false;
                for(PilotDTO p: r.pilots) if (p.Name.equals(c.getName())) {found = true;break;}
                if (found)
                    continue;

                // Retrieve all interesting data
                CharacterSheetResponse sheet = _eveApiCaller.GetCharacterSheet(k.KeyId, k.VCode, c.getCharacterID());
                SkillInTrainingResponse skillInTraining = _eveApiCaller.GetSkillInTraining(k.KeyId, k.VCode, c.getCharacterID());
                SkillQueueResponse skillQueue = _eveApiCaller.GetSkillQueue(k.KeyId, k.VCode, c.getCharacterID());

                // Calculate cache refresh date
                cachedUntils.add(new DateTime(sheet.getCachedUntil()));
                cachedUntils.add(new DateTime(skillInTraining.getCachedUntil()));
                cachedUntils.add(new DateTime(skillQueue.getCachedUntil()));

                // Calculate number of manufacturing & research slots
                long massProductionTypeId = 3387;
                long advancedMassProductionTypeId = 24625;
                long laboratoryOperationTypeId = 3406;
                long advancedLaboratoryOperationTypeId = 24624;
                int maxManufacturingJobs = 1;
                int maxResearchJobs = 1;
                    // also prepare list of type ids to retrieve
                List<Integer> typeIds = new ArrayList<Integer>();
                for(ApiSkill skill:sheet.getSkills())
                {
                    if (skill.getTypeID() == massProductionTypeId) maxManufacturingJobs += skill.getLevel();
                    if (skill.getTypeID() == advancedMassProductionTypeId) maxManufacturingJobs += skill.getLevel();
                    if (skill.getTypeID() == laboratoryOperationTypeId) maxResearchJobs += skill.getLevel();
                    if (skill.getTypeID() == advancedLaboratoryOperationTypeId) maxResearchJobs += skill.getLevel();
                    typeIds.add(skill.getTypeID());
                }
                if (skillInTraining.isSkillInTraining())
                    typeIds.add(skillInTraining.getTrainingTypeID());

                // Get type names
                Map<Integer, String> types = _typeNameDict.GetById(typeIds);
                PilotDTO p = new PilotDTO();
                p.Name = c.getName();
                p.CharacterId = sheet.getCharacterID();
                p.CurrentTrainingEnd = skillInTraining.getTrainingEndTime();
                if (skillInTraining.isSkillInTraining())
                    p.CurrentTrainingNameAndLevel = String.format("%s %d", types.get(skillInTraining.getTrainingTypeID()), skillInTraining.getTrainingToLevel());
                else
                    p.CurrentTrainingNameAndLevel = "";
                p.TrainingQueueEnd = new Date();
                for(ApiSkillQueueItem skill:skillQueue.getAll()) if (skill.getEndTime()!=null && skill.getEndTime().after(p.TrainingQueueEnd)) p.TrainingQueueEnd = skill.getEndTime();
                p.TrainingActive = skillInTraining.isSkillInTraining();
                p.MaxManufacturingJobs = maxManufacturingJobs;
                p.MaxResearchJobs = maxResearchJobs;
                p.KeyInfo = k;
                p.Skills = new ArrayList<String>();
                for(ApiSkill skill:sheet.getSkills()) p.Skills.add(String.format("%s %d", types.get(skill.getTypeID()), skill.getLevel()));
                r.pilots.add(p);
            }
        }

        r.cachedUntil = new NextRefreshCalculator().Calculate(new DateTime(), cachedUntils, _settingsRepo.getFrequencyinMinutes());
        logger.debug("PilotService cachedUntil {}", r.cachedUntil);

        return r;
    }
}
