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
import com.evenucleus.client.WalletRepo;
import com.evenucleus.client.WalletTransaction;

import junit.framework.Assert;

import org.easymock.EasyMock;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2015-01-01.
 */
public class WalletTransactionT extends TestBase {
    public void test_Pilots() throws SQLException, ApiException, ParseException {
        IPilotRepo pilotRepo = EasyMock.createMock(IPilotRepo.class);

        // Two pilots - first time
        EasyMock.expect(pilotRepo.GetAll()).andReturn(Arrays.<Pilot>asList(
                        new Pilot() {{
                            PilotId = 1;
                            CharacterId = 2;
                            KeyInfo = new com.evenucleus.client.KeyInfo() {{
                                KeyId = 1;
                                VCode = "vcode";
                            }};
                        }},
                        new Pilot() {{
                            PilotId = 3;
                            CharacterId = 4;
                            KeyInfo = new KeyInfo() {{
                                KeyId = 1;
                                VCode = "vcode";
                            }};
                        }}
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
        EasyMock.expect(eveApi.getWalletTransactions(1, "vcode", 2, 1, 0)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction(){{transactionID=100; price = 200;PilotId=1;}}
        ));
        // First call for the second pilot
        EasyMock.expect(eveApi.getWalletTransactions(1, "vcode", 4, 3, 0)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction() {{
                    transactionID = 200;
                    price = 400;
                    PilotId = 3;
                }}
        ));

        // Second call for the first pilot - new fromID is expected
        EasyMock.expect(eveApi.getWalletTransactions(1, "vcode", 2, 1, 100)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction() {{
                    transactionID = 101;
                    price = 300;
                    PilotId = 1;
                }}
        ));

        // second call for the second pilot
        EasyMock.expect(eveApi.getWalletTransactions(1, "vcode", 4, 3, 200)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction() {{
                    transactionID = 201;
                    price = 401;
                    PilotId = 3;
                }}
        ));

        EasyMock.replay(pilotRepo, eveApi);

        WalletRepo repo = new WalletRepo(_localdb, pilotRepo, null, eveApi);
        repo.ReplicateFromEve();
        repo.ReplicateFromEve();

        EasyMock.verify(pilotRepo, eveApi);

        List<WalletTransaction> entries = repo.GetAll();
        Assert.assertEquals(4, entries.size());

        Map<Long, WalletTransaction> mp = new HashMap<Long, WalletTransaction>();
        for(WalletTransaction j:entries) mp.put(j.transactionID, j);

        Assert.assertEquals(200.0, mp.get(100l).price);
        Assert.assertEquals(400.0, mp.get(200l).price);
        Assert.assertEquals(300.0, mp.get(101l).price);
        Assert.assertEquals(401.0, mp.get(201l).price);

        Assert.assertEquals(1, mp.get(100l).PilotId);
        Assert.assertEquals(3, mp.get(200l).PilotId);
        Assert.assertEquals(1, mp.get(101l).PilotId);
        Assert.assertEquals(3, mp.get(201l).PilotId);

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
        EasyMock.expect(eveApi.getWalletTransactionsCorpo(1, "vcode", 1, 0)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction() {{
                    transactionID = 100;
                    price = 200;
                    CorporationId = 1;
                }}
        ));
        // First call for the second pilot
        EasyMock.expect(eveApi.getWalletTransactionsCorpo(1, "vcode", 3, 0)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction(){{transactionID=200; price = 400;CorporationId=3;}}
        ));

        // Second call for the first pilot - new fromID is expected
        EasyMock.expect(eveApi.getWalletTransactionsCorpo(1, "vcode", 1, 100)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction(){{transactionID=101; price = 300;CorporationId=1;}}
        ));

        // second call for the second pilot
        EasyMock.expect(eveApi.getWalletTransactionsCorpo(1, "vcode", 3, 200)).andReturn(Arrays.<WalletTransaction>asList(
                new WalletTransaction(){{transactionID=201; price = 401;CorporationId=3;}}
        ));

        EasyMock.replay(corpoRepo, eveApi);

        WalletRepo repo = new WalletRepo(_localdb, null, corpoRepo, eveApi);
        repo.ReplicateFromEve();
        repo.ReplicateFromEve();

        EasyMock.verify(corpoRepo, eveApi);

        List<WalletTransaction> entries = repo.GetAll();
        Assert.assertEquals(4, entries.size());

        Map<Long, WalletTransaction> mp = new HashMap<Long, WalletTransaction>();
        for(WalletTransaction j:entries) mp.put(j.transactionID, j);

        Assert.assertEquals(200.0, mp.get(100l).price);
        Assert.assertEquals(400.0, mp.get(200l).price);
        Assert.assertEquals(300.0, mp.get(101l).price);
        Assert.assertEquals(401.0, mp.get(201l).price);

        Assert.assertEquals(1, mp.get(100l).CorporationId);
        Assert.assertEquals(3, mp.get(200l).CorporationId);
        Assert.assertEquals(1, mp.get(101l).CorporationId);
        Assert.assertEquals(3, mp.get(201l).CorporationId);
    }

}
