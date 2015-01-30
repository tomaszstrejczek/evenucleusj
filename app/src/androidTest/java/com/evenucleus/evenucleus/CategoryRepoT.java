package com.evenucleus.evenucleus;

import com.evenucleus.client.CategoryRepo;
import com.evenucleus.client.StringProvider;
import com.evenucleus.client.UserException;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public class CategoryRepoT extends TestBase {
    public void test_EmptyCategoryList() throws SQLException {
        CategoryRepo categoryRepo = new CategoryRepo();
        categoryRepo._localdb = _localdb;
        categoryRepo._stringProvider = new StringProvider(getContext());
        List<String> list = categoryRepo.Get();
        Assert.assertEquals(1, list.size());   // predefined ones
    }

    public void test_AddCategory() throws SQLException, UserException {
        CategoryRepo categoryRepo = new CategoryRepo();
        categoryRepo._localdb = _localdb;
        categoryRepo._stringProvider = new StringProvider(getContext());
        categoryRepo.AddCategory("ala");

        List<String> list = categoryRepo.Get();
        Assert.assertEquals(2, list.size()); // additional one predefined
        boolean found = false;
        for(String s: list)
            if (s.equals("ala"))
                found = true;

        Assert.assertTrue(found);
    }

    public void test_AddCDuplicateategory() throws SQLException, UserException {
        CategoryRepo categoryRepo = new CategoryRepo();
        categoryRepo._localdb = _localdb;
        categoryRepo._stringProvider = new StringProvider(getContext());
        categoryRepo.AddCategory("ala");
        try
        {
            categoryRepo.AddCategory("ala");
            Assert.assertTrue("Unreachable", false);
        }
        catch (UserException e)
        {
        }
    }

}
