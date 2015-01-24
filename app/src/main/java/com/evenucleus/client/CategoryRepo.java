package com.evenucleus.client;

import android.util.Log;

import com.evenucleus.evenucleus.MyDatabaseHelper;
import com.evenucleus.evenucleus.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
@EBean
public class CategoryRepo implements ICategoryRepo {
    @Bean(MyDatabaseHelper.class)
    public DatabaseHelper _localdb;

    @Bean(StringProvider.class)
    public IStringProvider _stringProvider;

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
