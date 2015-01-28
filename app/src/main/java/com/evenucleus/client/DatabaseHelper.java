package com.evenucleus.client;

/**
 * Created by tomeks on 2014-12-28.
 */

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;

import java.io.File;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
@EBean
public class DatabaseHelper
{
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;
    private String _fullPath;
    public SQLiteDatabase _database;
    private ConnectionSource _connectionSource;

    // the DAO object we use to access the SimpleData table
    private Dao<VersionM, Integer> versionDao = null;
    private Dao<KeyInfo, Integer> keyInfoDao = null;
    private Dao<Pilot, Integer> pilotDao = null;
    private Dao<Skill, Integer> skillDao = null;
    private Dao<PendingNotification, Integer> pendingNotificationDao = null;
    private Dao<Corporation, Integer> corporationDao = null;
    private Dao<Job, Integer> jobDao = null;
    private Dao<Category, Integer> categoryDao = null;
    private Dao<CacheEntry, String> cacheEntryDao = null;
    private Dao<TypeNameEntry, Integer> typeNameEntryDao = null;
    private Dao<JournalEntry, Integer> journalEntryDao = null;
    private Dao<WalletTransaction, Integer> walletTransactionDao = null;
    private Dao<Setting, String> setttingDao = null;

    public void Initialize(String fullPath) throws java.sql.SQLException {
        Log.i(DatabaseHelper.class.getName(), String.format("Initialize %s", fullPath));

        _fullPath = fullPath;
        File f = new File(_fullPath);
        File f2 = new File(f.getParent());
        f2.mkdirs();

        _database = SQLiteDatabase.openOrCreateDatabase(_fullPath, null);
        _connectionSource = new AndroidConnectionSource(_database);
        boolean initialized = DoesTableExists(_database, "VersionM");
        if (!initialized)
            CreateInitial();
    }


    private boolean DoesTableExists(SQLiteDatabase database, String tablename)
    {
        long cnt = DatabaseUtils.queryNumEntries(database, "sqlite_master", "type='table' AND name=?", new String[] {tablename});
        return cnt > 0;
    }

    private void CreateInitial() throws java.sql.SQLException {
        TableUtils.createTable(_connectionSource, VersionM.class);
        TableUtils.createTable(_connectionSource, KeyInfo.class);
        TableUtils.createTable(_connectionSource, Pilot.class);
        TableUtils.createTable(_connectionSource, Corporation.class);
        TableUtils.createTable(_connectionSource, Skill.class);
        TableUtils.createTable(_connectionSource, PendingNotification.class);
        TableUtils.createTable(_connectionSource, Job.class);
        TableUtils.createTable(_connectionSource, Category.class);
        TableUtils.createTable(_connectionSource, CacheEntry.class);
        TableUtils.createTable(_connectionSource, TypeNameEntry.class);
        TableUtils.createTable(_connectionSource, JournalEntry.class);
        TableUtils.createTable(_connectionSource, WalletTransaction.class);
        TableUtils.createTable(_connectionSource, Setting.class);

        Category cat = new Category();
        cat.Name = "Arbitrage";
        getCategoryDao().createOrUpdate(cat);
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<VersionM, Integer> getVersionDao() throws java.sql.SQLException {
        if (versionDao == null) {
            versionDao = DaoManager.createDao(_connectionSource, VersionM.class);
        }
        return versionDao;
    }

    public Dao<KeyInfo, Integer> getKeyInfoDao() throws java.sql.SQLException {
        if (keyInfoDao == null) {
            keyInfoDao = DaoManager.createDao(_connectionSource, KeyInfo.class);
        }
        return keyInfoDao;
    }

    public Dao<Pilot, Integer> getPilotDao() throws java.sql.SQLException {
        if (pilotDao == null) {
            pilotDao = DaoManager.createDao(_connectionSource, Pilot.class);
        }
        return pilotDao;
    }

    public Dao<Skill, Integer> getSkillDao() throws java.sql.SQLException {
        if (skillDao == null) {
            skillDao = DaoManager.createDao(_connectionSource, Skill.class);
        }
        return skillDao;
    }

    public Dao<PendingNotification, Integer> getPendingNotificationDao() throws java.sql.SQLException {
        if (pendingNotificationDao == null) {
            pendingNotificationDao = DaoManager.createDao(_connectionSource, PendingNotification.class);
        }
        return pendingNotificationDao;
    }

    public Dao<Corporation, Integer> getCorporationDao() throws java.sql.SQLException {
        if (corporationDao == null) {
            corporationDao = DaoManager.createDao(_connectionSource, Corporation.class);
        }
        return corporationDao;
    }

    public Dao<Job, Integer> getJobDao() throws java.sql.SQLException {
        if (jobDao == null) {
            jobDao = DaoManager.createDao(_connectionSource, Job.class);
        }
        return jobDao;
    }

    public Dao<Category, Integer> getCategoryDao() throws java.sql.SQLException {
        if (categoryDao == null) {
            categoryDao = DaoManager.createDao(_connectionSource, Category.class);
        }
        return categoryDao;
    }

    public Dao<CacheEntry, String> getCacheEntryDao() throws java.sql.SQLException {
        if (cacheEntryDao == null) {
            cacheEntryDao = DaoManager.createDao(_connectionSource, CacheEntry.class);
        }
        return cacheEntryDao;
    }

    public Dao<TypeNameEntry, Integer> getTypeNameEntryDao() throws java.sql.SQLException {
        if (typeNameEntryDao == null) {
            typeNameEntryDao = DaoManager.createDao(_connectionSource, TypeNameEntry.class);
        }
        return typeNameEntryDao;
    }

    public Dao<JournalEntry, Integer> getJournalEntryDao() throws java.sql.SQLException {
        if (journalEntryDao== null) {
            journalEntryDao = DaoManager.createDao(_connectionSource, JournalEntry.class);
        }
        return journalEntryDao;
    }

    public Dao<WalletTransaction, Integer> getWalletTransactionDao() throws java.sql.SQLException {
        if (walletTransactionDao== null) {
            walletTransactionDao = DaoManager.createDao(_connectionSource, WalletTransaction.class);
        }
        return walletTransactionDao;
    }

    public Dao<Setting, String> getSettingDao() throws java.sql.SQLException {
        if (setttingDao== null) {
            setttingDao = DaoManager.createDao(_connectionSource, Setting.class);
        }
        return setttingDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    public void close() {
        _database.close();
        versionDao = null;
    }
}

