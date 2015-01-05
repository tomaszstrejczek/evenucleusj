package com.evenucleus.mocks;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.UserData;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tomeks on 2015-01-04.
 */
@EBean
public class PilotRepoMock implements IPilotRepo {
    @Override
    public void SimpleUpdateFromKey(int keyid, String vcode) throws ApiException, SQLException {
    }

    @Override
    public void Update(UserData data) throws SQLException {
    }

    @Override
    public List<Pilot> GetAll() throws SQLException {
        return Arrays.asList(
                new Pilot() {{
                    Name = "stryju";
                    CharacterId = 3003981;
                    CurrentTrainingNameAndLevel = "Minmatar Starship Engineering V";
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 10);
                    cal.add(Calendar.HOUR, 7);
                    cal.add(Calendar.MINUTE, 2);
                    CurrentTrainingEnd = cal.getTime();
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                    TrainingQueueEnd = cal.getTime();
                    TrainingActive = true;
                }},
                new Pilot() {{
                    Name = "a99990 Papotte";
                    CharacterId = 3004513;
                    CurrentTrainingNameAndLevel = "Minmatar Starship Engineering V";
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 12);
                    cal.add(Calendar.HOUR, 23);
                    cal.add(Calendar.MINUTE, 7);
                    CurrentTrainingEnd = cal.getTime();
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                    TrainingQueueEnd = cal.getTime();
                    TrainingActive = true;
                }},
                new Pilot() {{
                    Name = "a99990 Papottex";
                    CharacterId = 3004513;
                    CurrentTrainingNameAndLevel = "Minmatar Starship Engineering V";
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    cal.add(Calendar.HOUR, 0);
                    cal.add(Calendar.MINUTE, 4);
                    CurrentTrainingEnd = cal.getTime();
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                    TrainingQueueEnd = cal.getTime();
                    TrainingActive = false;
                }}
        );
    }

    @Override
    public void SetFreeManufacturingJobsNofificationCount(int pilotid, int value) {
    }

    @Override
    public void SetFreeResearchJobsNofificationCount(int pilotid, int value) {
    }
}
