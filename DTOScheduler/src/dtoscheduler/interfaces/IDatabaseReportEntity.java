/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.interfaces;

/**
 *
 * @author Daniel
 */
public interface IDatabaseReportEntity extends IDatabaseEntity
{
    int getColumnCount();

    Class<?> getColumnType(int columnNumber);

    Object getColumnValue(int columnNumber);

    String getColumnName(int columnNumber);
}
