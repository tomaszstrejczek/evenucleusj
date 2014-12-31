package com.evenucleus.evenucleus;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.Corporation;
import com.evenucleus.client.ICorporationRepo;
import com.evenucleus.client.IEveApiCaller;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.JournalEntry;
import com.evenucleus.client.JournalRepo;
import com.evenucleus.client.KeyInfo;
import com.evenucleus.client.Pilot;

import org.easymock.EasyMock;
import org.easymock.IMockBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by tomeks on 2014-12-31.
 */
public class JournalRepoT extends TestBase {
    public void test_Pilots() throws SQLException, ApiException, ParseException {
        IPilotRepo pilotRepo = EasyMock.createMock(IPilotRepo.class);

        // Two pilots - first time
        EasyMock.expect(pilotRepo.GetAll()).andReturn(Arrays.<Pilot>asList(
                new Pilot(){{PilotId=1;CharacterId=2;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}},
                new Pilot(){{PilotId=3;CharacterId=4;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}}
            )
        );
        // Two pilots - second time
        EasyMock.expect(pilotRepo.GetAll()).andReturn(Arrays.<Pilot>asList(
                        new Pilot(){{PilotId=1;CharacterId=2;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}},
                        new Pilot(){{PilotId=3;CharacterId=4;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}}
                )
        );

        IEveApiCaller eveApi = EasyMock.createMock(IEveApiCaller.class);
        // First call for the first pilot
        EasyMock.expect(eveApi.getJournalEntries(1, "vcode", 2, 1, 0)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=100; amount = 200;PilotId=1;}}
        ));
        // First call for the second pilot
        EasyMock.expect(eveApi.getJournalEntries(1, "vcode", 4, 3, 0)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=200; amount = 400;PilotId=3;}}
        ));

        // Second call for the first pilot - new fromID is expected
        EasyMock.expect(eveApi.getJournalEntries(1, "vcode", 2, 1, 100)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=101; amount = 300;PilotId=1;}}
        ));

        // second call for the second pilot
        EasyMock.expect(eveApi.getJournalEntries(1, "vcode", 4, 3, 200)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=201; amount = 400;PilotId=3;}}
        ));

        EasyMock.replay(pilotRepo, eveApi);

        JournalRepo journalRepo = new JournalRepo(_localdb, pilotRepo, null, eveApi);
        journalRepo.ReplicateFromEve();
        journalRepo.ReplicateFromEve();

        EasyMock.verify(pilotRepo, eveApi);
    }

    public void test_Corporations() throws SQLException, ApiException, ParseException {
        ICorporationRepo corpoRepo = EasyMock.createMock(ICorporationRepo.class);

        // Two pilots - first time
        EasyMock.expect(corpoRepo.GetAll()).andReturn(Arrays.<Corporation>asList(
                        new Corporation(){{CorporationId=1;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}},
                        new Corporation(){{CorporationId=3;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}}
                )
        );
        // Two pilots - second time
        EasyMock.expect(corpoRepo.GetAll()).andReturn(Arrays.<Corporation>asList(
                new Corporation(){{CorporationId=1;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}},
                new Corporation(){{CorporationId=3;KeyInfo=new KeyInfo() {{KeyId=1;VCode="vcode";}};}}
                )
        );

        IEveApiCaller eveApi = EasyMock.createMock(IEveApiCaller.class);
        // First call for the first pilot
        EasyMock.expect(eveApi.getJournalEntriesCorpo(1, "vcode", 1, 0)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=100; amount = 200;CorporationId=1;}}
        ));
        // First call for the second pilot
        EasyMock.expect(eveApi.getJournalEntriesCorpo(1, "vcode", 3, 0)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=200; amount = 400;CorporationId=3;}}
        ));

        // Second call for the first pilot - new fromID is expected
        EasyMock.expect(eveApi.getJournalEntriesCorpo(1, "vcode", 1, 100)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=101; amount = 300;CorporationId=1;}}
        ));

        // second call for the second pilot
        EasyMock.expect(eveApi.getJournalEntriesCorpo(1, "vcode", 3, 200)).andReturn(Arrays.<JournalEntry>asList(
                new JournalEntry(){{refID=201; amount = 400;CorporationId=3;}}
        ));

        EasyMock.replay(corpoRepo, eveApi);

        JournalRepo journalRepo = new JournalRepo(_localdb, null, corpoRepo, eveApi);
        journalRepo.ReplicateFromEve();
        journalRepo.ReplicateFromEve();

        EasyMock.verify(corpoRepo, eveApi);
    }

}
