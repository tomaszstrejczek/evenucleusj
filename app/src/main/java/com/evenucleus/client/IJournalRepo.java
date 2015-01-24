package com.evenucleus.client;

import com.beimin.eveapi.exception.ApiException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by tomeks on 2014-12-31.
 */
public interface IJournalRepo {
    void ReplicateFromEve() throws SQLException, ParseException, ApiException;
    List<JournalEntry> GetAll() throws SQLException;
    void AssignCategory(int journalEntryId, String category) throws SQLException;
}
