package com.evenucleus.client;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.shared.industryjobs.ApiIndustryJob;
import com.beimin.eveapi.shared.industryjobs.IndustryJobsResponse;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class JobService implements IJobService {
    final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Bean(PilotRepo.class)
    public IPilotRepo _pilotRepo;

    @Bean(CorporationRepo.class)
    public ICorporationRepo _corporationRepo;

    @Bean(EveApiCaller.class)
    public IEveApiCaller _eveApiCaller;

    @Bean(TypeNameDict.class)
    public ITypeNameDict _typeNameDict;

    @Bean(SettingsRepo.class)
    public ISettingsRepo _settingsRepo;

    @Override
    public Result Get() throws SQLException, ApiException {
        logger.debug("Get");

        List<DateTime> cachedUntils = new ArrayList<DateTime>();

        Result result = new Result();
        result.cachedUntil = new DateTime().plusHours(1);
        result.jobs = new ArrayList<Job>();

        List<ApiIndustryJob> tmpResult = new ArrayList<ApiIndustryJob>();

        List<Pilot> pilots = _pilotRepo.GetAll();
        for(Pilot p:pilots) {
            if (p.KeyInfo==null)
                continue;
            IndustryJobsResponse jobs = _eveApiCaller.getIndustryJobs(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId);
            cachedUntils.add(new DateTime(jobs.getCachedUntil()));
            tmpResult.addAll(jobs.getAll());
        }

        List<Corporation> corps = _corporationRepo.GetAll();
        for(Corporation c:corps) {
            if (c.KeyInfo == null)
                continue;
            IndustryJobsResponse jobs = _eveApiCaller.getIndustryJobsCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode);
            cachedUntils.add(new DateTime(jobs.getCachedUntil()));
            tmpResult.addAll(jobs.getAll());
        }

        Set<Integer> typeIds = new HashSet<Integer>();
        for(ApiIndustryJob j: tmpResult) if (!typeIds.contains(j.getBlueprintTypeID())) typeIds.add(j.getBlueprintTypeID());
        Map<Integer,String> typeIdsMap = _typeNameDict.GetById(typeIds);

        for(ApiIndustryJob j:tmpResult) {
            Job job = new Job();
            Duration tillNow = new Duration(new DateTime(j.getBeginProductionTime()), new DateTime());
            Duration total = new Duration(new DateTime(j.getBeginProductionTime()), new DateTime(j.getEndProductionTime()));
            job.PercentageOfCompletion = tillNow.getMillis() < 0 || total.getMillis() <= 0 ? 0: (int) (100*tillNow.getMillis() / total.getMillis());
            job.JobCompleted =  j.getEndProductionTime()==null || !new Date().before(j.getEndProductionTime());
            job.JobDescription = String.format("%s %s %d", typeIdsMap.get(j.getBlueprintTypeID()), GetActivityAnnotation(j.getActivityID()),j.getRuns());
            job.Owner = j.getInstallerName();
            job.IsManufacturing = j.getActivityID() == 1;
            job.JobEnd = j.getEndProductionTime();
            job.Url = String.format("https://image.eveonline.com/Type/%d_64.png", j.getBlueprintTypeID());
            result.jobs.add(job);
        }

        result.cachedUntil = new NextRefreshCalculator().Calculate(new DateTime(), cachedUntils, _settingsRepo.getFrequencyinMinutes());
        logger.debug("JobService cachedUntil {}", result.cachedUntil);

        return result;
    }

    private String GetActivityAnnotation(int activity)
    {
        switch (activity)
        {
            case 1:
                return "x";
            case 3:
                return "TE";
            case 4:
                return "ME";
            case 5:
                return "CP";
            case 7:
                return "RE";
            default:
                return String.format("?%d?", activity);
        }
    }

}
