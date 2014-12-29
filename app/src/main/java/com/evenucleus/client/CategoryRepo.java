package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public class CategoryRepo implements ICategoryRepo {
    DatabaseHelper _localdb;
    IStringProvider _stringProvider;

    public CategoryRepo(DatabaseHelper localdb, IStringProvider stringProvider) {
        _localdb = localdb;
        _stringProvider = stringProvider;
    }

    @Override
    public void AddCategory(String name) throws SQLException, UserException {
        Log.d(CategoryRepo.class.getName(), String.format("adding category %s", name));

        long cnt = _localdb.getCategoryDao().queryBuilder().where().eq("Name", name).countOf();

        if (cnt > 0)
            throw new UserException(_stringProvider.Get(R.string.ErrorCategoryAlreadyDefined));

        Category c = new Category();
        c.Name = name;
        _localdb.getCategoryDao().createOrUpdate(c);
    }

    @Override
    public List<String> Get() throws SQLException {
        Log.d(CategoryRepo.class.getName(), "GetAll");
        List<String> result = new ArrayList<String>();
        for(Category c:_localdb.getCategoryDao().queryForAll())
            result.add(c.Name);

        return result;
    }
}
