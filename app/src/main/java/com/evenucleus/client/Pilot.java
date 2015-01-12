package com.evenucleus.client;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by tomeks on 2014-12-28.
 */
public class Pilot {

    public void setFromPilotDTO(PilotDTO dto) throws SQLException {
        CharacterId = dto.CharacterId;
        Name = dto.Name;
        CurrentTrainingNameAndLevel = dto.CurrentTrainingNameAndLevel;
        CurrentTrainingEnd = dto.CurrentTrainingEnd;
        TrainingQueueEnd = dto.TrainingQueueEnd;
        TrainingActive = dto.TrainingActive;
        MaxManufacturingJobs = dto.MaxManufacturingJobs;
        MaxResearchJobs = dto.MaxResearchJobs;
        KeyInfo = dto.KeyInfo;
        // Skills are handled by SkillRepo
    }


    @DatabaseField(generatedId = true)
    public int PilotId;

    @DatabaseField(unique = true)
    public long CharacterId;

    @DatabaseField(unique = true)
    public String Name;

    public String getUrl() {
        return String.format("https://image.eveonline.com/Character/%d_64.jpg", CharacterId);
    }

    @DatabaseField
    public String CurrentTrainingNameAndLevel;
    @DatabaseField
    public Date CurrentTrainingEnd;
    @DatabaseField
    public Date TrainingQueueEnd;
    @DatabaseField
    public boolean TrainingActive;

    @DatabaseField
    public int MaxManufacturingJobs;
    @DatabaseField
    public int MaxResearchJobs;

    @DatabaseField
    public int FreeManufacturingJobsNofificationCount;
    @DatabaseField
    public int FreeResearchJobsNofificationCount;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    public KeyInfo KeyInfo;

    @ForeignCollectionField(columnName = "Skills")
    public ForeignCollection<Skill> Skills;
}
