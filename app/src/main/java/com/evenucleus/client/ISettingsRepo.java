package com.evenucleus.client;

import java.sql.SQLException;
import java.util.Date;
/**
 * Created by tomeks on 2015-01-18.
 */
public interface ISettingsRepo {
    public Date getNextAlert() throws SQLException;
    public void setNextAlert(Date value) throws SQLException;

    public Date getLatestAlert() throws SQLException;
    public void setLatestAlert(Date value) throws SQLException;

    public Date getFinancialsLaterThan() throws SQLException;
    public void setFinancialsLaterThan(Date value) throws SQLException;
}
