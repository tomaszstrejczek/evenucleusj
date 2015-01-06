package com.evenucleus.evenucleus;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.TypeNameDict;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by tomeks on 2014-12-31.
 */
public class TypeNameDictT extends TestBase {
    public void test_SimpleCall() throws SQLException, ApiException {
        TypeNameDict typeNameDict = new TypeNameDict();
        typeNameDict._localdb = _localdb;

        Map<Integer, String> r = typeNameDict.GetById(Arrays.asList(12345));
        Assert.assertEquals("200mm Railgun I Blueprint", r.get(12345));

        r = typeNameDict.GetById(Arrays.asList(12345, 34317));
        Assert.assertEquals("200mm Railgun I Blueprint", r.get(12345));
        Assert.assertEquals("Confessor", r.get(34317));

    }

}
