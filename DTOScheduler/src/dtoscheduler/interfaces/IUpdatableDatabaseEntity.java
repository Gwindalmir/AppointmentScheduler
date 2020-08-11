/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.interfaces;

import dtoscheduler.singleton.GlobalInformation;
import java.sql.SQLException;

/**
 *
 * @author Daniel
 */
public interface IUpdatableDatabaseEntity extends IDatabaseEntity
{
    void update() throws SQLException;

    void add() throws SQLException;

    void delete() throws SQLException;

    void refresh() throws SQLException;

    /**
     * Looks up the entry with the name, as refresh(), but doesn't throw an
     * error.
     *
     * @return true if lookup successful, false if not.
     */
    default boolean lookup()
    {
        try
        {
            refresh();
        }
        catch (SQLException ex)
        {
            return false;
        }
        return true;
    }
    
    default void markDirty() throws SQLException
    {
        GlobalInformation.getInstance().setDirty(true);
    }
}
