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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
@EBean
public class DatabaseHelper
{
    final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);
    final String db_version1 = "Version1";

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
    private Dao<JournalEntry, Long> journalEntryDao = null;
    private Dao<WalletTransaction, Long> walletTransactionDao = null;
    private Dao<Setting, String> setttingDao = null;

    public void Initialize(String fullPath) throws java.sql.SQLException {
        logger.info("Initialize {}", fullPath);

        _fullPath = fullPath;
        File f = new File(_fullPath);
        File f2 = new File(f.getParent());
        f2.mkdirs();

        _database = SQLiteDatabase.openOrCreateDatabase(_fullPath, null);
        _connectionSource = new AndroidConnectionSource(_database);
        boolean initialized = DoesTableExists(_database, "VersionM");
        if (!initialized)
            CreateInitial();

        HashSet<String> versions = new HashSet<String>();
        for(VersionM v: getVersionDao().queryForAll()) {
            logger.info("Installed version {}", v.Name);
            versions.add(v.Name);
        }

        if (!versions.contains(db_version1))
            UpgradeToVersion1();
    }


    private boolean DoesTableExists(SQLiteDatabase database, String tablename)
    {
        long cnt = DatabaseUtils.queryNumEntries(database, "sqlite_master", "type='table' AND name=? COLLATE NOCASE", new String[] {tablename});
        return cnt > 0;
    }

    // Changing primary key of wallettransaction and journal entry
    private void UpgradeToVersion1() throws SQLException {
        logger.info("UpgradeToVersion1");
        upgradeToVersion1_jes();
        upgradeToVersion1_wts();

        VersionM ver = new VersionM();
        ver.Name = db_version1;
        getVersionDao().createOrUpdate(ver);
    }
    private void upgradeToVersion1_jes() throws SQLException {
        logger.info("UpgradeToVersion1_jes");
        // We can load all records because we only removed one column
        List<JournalEntry> jes = getJournalEntryDao().queryForAll();
        _database.execSQL("alter table journalentry rename to journalentry_old");
        logger.info("journalentry renamed");
        try {
            _database.execSQL("CREATE TABLE `journalentry` (`CategoryName` VARCHAR , `RefTypeName` VARCHAR , `argName1` VARCHAR , `date` VARCHAR , `ownerName1` VARCHAR , `ownerName2` VARCHAR , `reason` VARCHAR , `taxAmount` DOUBLE PRECISION , `taxReceiverID` BIGINT , `CorporationId` INTEGER , `amount` DOUBLE PRECISION , `argID1` BIGINT , `balance` DOUBLE PRECISION , `ownerID1` BIGINT , `ownerID2` BIGINT , `refID` BIGINT , `PilotId` INTEGER , `refTypeID` INTEGER , PRIMARY KEY (`refID`) )");
            logger.info("new journalentry table created");

            Dao<JournalEntry, Long> jesDao = getJournalEntryDao();
            Map<Long, JournalEntry> saved = new HashMap<Long, JournalEntry>();
            int cnt = 0;
            for (JournalEntry je : jes) {
                ++cnt;
                if (cnt % 100 == 0) {
                    logger.debug("Processed {} journalentry records", cnt);
                }
                if (saved.containsKey(je.refID)) {
                    JournalEntry prev = saved.get(je.refID);
                    if ((prev.CategoryName == null || prev.CategoryName.isEmpty()) && je.CategoryName != null && !je.CategoryName.isEmpty()) {
                        prev.CategoryName = je.CategoryName;
                        jesDao.createOrUpdate(prev);
                    }
                } else {
                    saved.put(je.refID, je);
                    jesDao.createOrUpdate(je);
                }
            }
        } catch (Exception e) {
            logger.error("exception caught {}", e);
            if (DoesTableExists(_database, "journalentry"))
                _database.execSQL("drop table journalentry");
            _database.execSQL("alter table journalentry_old rename to journalentry");
            throw e;
        }
    }

    private void upgradeToVersion1_wts() throws SQLException {
        logger.info("UpgradeToVersion1_wts");
        // We can load all records because we only removed one column
        List<WalletTransaction> wts = getWalletTransactionDao().queryForAll();
        _database.execSQL("alter table wallettransaction rename to wallettransaction_old");
        logger.info("wallettransaction renamed");
        try {
            _database.execSQL("CREATE TABLE `wallettransaction` (`characterID` BIGINT , `characterName` VARCHAR , `clientName` VARCHAR , `stationName` VARCHAR , `transactionDateTime` VARCHAR , `transactionFor` VARCHAR , `transactionType` VARCHAR , `typeName` VARCHAR , `clientID` BIGINT , `journalTransactionID` BIGINT , `price` DOUBLE PRECISION , `transactionID` BIGINT , `CategoryId` INTEGER , `CorporationId` INTEGER , `PilotId` INTEGER , `quantity` INTEGER , `stationID` INTEGER , `typeID` INTEGER , PRIMARY KEY (`transactionID`) )");
            logger.info("new wallettransaction table created");

            Dao<WalletTransaction, Long> wtsDao = getWalletTransactionDao();
            Map<Long, WalletTransaction> saved = new HashMap<Long, WalletTransaction>();
            int cnt = 0;
            for (WalletTransaction w : wts) {
                ++cnt;
                if (cnt % 100 == 0) {
                    logger.debug("Processed {} wallettransaction records", cnt);
                }
                if (!saved.containsKey(w.transactionID)) {
                    saved.put(w.transactionID, w);
                    wtsDao.createOrUpdate(w);
                }
            }

        } catch (Exception e) {
            logger.error("exception caught {}", e);
            if (DoesTableExists(_database, "wallettransaction"))
                _database.execSQL("drop table wallettransaction");
            _database.execSQL("alter table wallettransaction_old rename to wallettransaction");
            throw e;
        }
    }

    private void CreateInitial() throws java.sql.SQLException {
        logger.debug("CreateInitial");
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

        logger.debug("CreateInitial finished");
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

    public Dao<JournalEntry, Long> getJournalEntryDao() throws java.sql.SQLException {
        if (journalEntryDao== null) {
            journalEntryDao = DaoManager.createDao(_connectionSource, JournalEntry.class);
        }
        return journalEntryDao;
    }

    public Dao<WalletTransaction, Long> getWalletTransactionDao() throws java.sql.SQLException {
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

