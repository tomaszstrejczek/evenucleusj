package com.beimin.eveapi.shared.industryjobs;

import org.xml.sax.Attributes;

import com.beimin.eveapi.core.AbstractContentListHandler;

public class IndustryJobsHandler extends AbstractContentListHandler<IndustryJobsResponse, ApiIndustryJob> {

	public IndustryJobsHandler() {
		super(IndustryJobsResponse.class);
	}

	@Override
	protected ApiIndustryJob getItem(Attributes attrs) {
		ApiIndustryJob job = new ApiIndustryJob();
		job.setJobID(getLong(attrs, "jobID"));
		job.setContainerID(getLong(attrs, "containerID"));
		job.setInstalledItemID(getLong(attrs, "installedItemID"));
		job.setInstalledItemLocationID(getLong(attrs, "installedItemLocationID"));
		job.setInstalledItemQuantity(getInt(attrs, "installedItemQuantity"));
		job.setInstalledItemProductivityLevel(getInt(attrs, "installedItemProductivityLevel"));
		job.setInstalledItemMaterialLevel(getInt(attrs, "installedItemMaterialLevel"));
		job.setInstalledItemLicensedProductionRunsRemaining(getInt(attrs, "installedItemLicensedProductionRunsRemaining"));
		job.setOutputLocationID(getLong(attrs, "outputLocationID"));
		job.setInstallerID(getLong(attrs, "installerID"));
        job.setInstallerName(getString(attrs, "installerName"));
		job.setRuns(getInt(attrs, "runs"));
		job.setLicensedProductionRuns(getInt(attrs, "licensedProductionRuns"));
		job.setInstalledInSolarSystemID(getLong(attrs, "installedInSolarSystemID"));
		job.setContainerLocationID(getLong(attrs, "containerLocationID"));
		job.setMaterialMultiplier(getDouble(attrs, "materialMultiplier"));
		job.setCharMaterialMultiplier(getDouble(attrs, "charMaterialMultiplier"));
		job.setTimeMultiplier(getDouble(attrs, "timeMultiplier"));
		job.setCharTimeMultiplier(getDouble(attrs, "charTimeMultiplier"));
		job.setBlueprintTypeID(getInt(attrs, "blueprintTypeID"));
		job.setOutputTypeID(getInt(attrs, "outputTypeID"));
		job.setContainerTypeID(getInt(attrs, "containerTypeID"));
		job.setInstalledItemCopy(getLong(attrs, "installedItemCopy"));
		job.setCompleted(getBoolean(attrs, "completed"));
		job.setCompletedSuccessfully(getBoolean(attrs, "completedSuccessfully"));
		job.setInstalledItemFlag(getInt(attrs, "installedItemFlag"));
		job.setOutputFlag(getInt(attrs, "outputFlag"));
		job.setActivityID(getInt(attrs, "activityID"));
		job.setCompletedStatus(getInt(attrs, "status"));
		job.setInstallTime(getDate(attrs, "installTime"));
		job.setBeginProductionTime(getDate(attrs, "startDate"));
		job.setEndProductionTime(getDate(attrs, "endDate"));
		job.setPauseProductionTime(getDate(attrs, "pauseDate"));
        job.setTimeInSeconds(getInt(attrs, "timeInSeconds"));
		return job;
	}

}