package com.evenucleus.client;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.shared.industryjobs.ApiIndustryJob;
import com.beimin.eveapi.shared.industryjobs.IndustryJobsResponse;

/**
 * Created by tomeks on 2015-01-11.
 */
@EBean
public class JobService implements IJobService {

    @Bean(PilotRepo.class)
    IPilotRepo _pilotRepo;

    @Bean(CorporationRepo.class)
    ICorporationRepo _corporationRepo;

    @Bean(EveApiCaller.class)
    IEveApiCaller _eveApiCaller;

    @Bean(TypeNameDict.class)
    ITypeNameDict _typeNameDict;


    @Override
    public Result Get() throws SQLException, ApiException {
        Log.d(JobService.class.getName(), "Get");
        Result result = new Result();
        result.cachedUntil = new DateTime().plusHours(1);
        result.jobs = new ArrayList<Job>();

        List<ApiIndustryJob> tmpResult = new ArrayList<ApiIndustryJob>();

        List<Pilot> pilots = _pilotRepo.GetAll();
        for(Pilot p:pilots) {
            IndustryJobsResponse jobs = _eveApiCaller.getIndustryJobs(p.KeyInfo.KeyId, p.KeyInfo.VCode, p.CharacterId);
            if (result.cachedUntil.isAfter(new DateTime(jobs.getCachedUntil()))) result.cachedUntil = new DateTime(jobs.getCachedUntil());
            tmpResult.addAll(jobs.getAll());
        }

        List<Corporation> corps = _corporationRepo.GetAll();
        for(Corporation c:corps) {
            IndustryJobsResponse jobs = _eveApiCaller.getIndustryJobsCorpo(c.KeyInfo.KeyId, c.KeyInfo.VCode);
            if (result.cachedUntil.isAfter(new DateTime(jobs.getCachedUntil()))) result.cachedUntil = new DateTime(jobs.getCachedUntil());
            tmpResult.addAll(jobs.getAll());
        }

        Set<Integer> typeIds = new HashSet<Integer>();
        for(ApiIndustryJob j: tmpResult) if (!typeIds.contains(j.getInstalledItemTypeID())) typeIds.add(j.getInstalledItemTypeID());
        Map<Integer,String> typeIdsMap = _typeNameDict.GetById(typeIds);

        for(ApiIndustryJob j:tmpResult) {
            Job job = new Job();
            
        }

        return null;
    }
}
