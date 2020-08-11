/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

import java.time.Duration;

/*
This object maps the result set from this VIEW:

CREATE OR REPLACE VIEW REPORT_TIME_SPENT_BY_CONSULTANT AS 
SELECT user.userName, SUM(TIMESTAMPDIFF(SECOND, appointment.start, appointment.end)) as totalTimeSeconds
FROM user INNER JOIN appointment ON appointment.userId = user.userId 
GROUP BY user.userName;

 */
/**
 *
 * @author Daniel
 */
public class ReportTimeSpentByConsultant implements dtoscheduler.interfaces.IDatabaseReportEntity
{
    private String userName;
    private Duration totalTime;

    public ReportTimeSpentByConsultant()
    {
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Duration getTotalTime()
    {
        return totalTime;
    }

    public void setTotalTime(Duration totalTimeSeconds)
    {
        this.totalTime = totalTimeSeconds;
    }

    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public Class<?> getColumnType(int columnNumber)
    {
        switch (columnNumber)
        {
            case 0:
                return userName.getClass();
            case 1:
                return totalTime.getClass();
            default:
                throw new ArrayIndexOutOfBoundsException(columnNumber);
        }
    }

    @Override
    public Object getColumnValue(int columnNumber)
    {
        switch (columnNumber)
        {
            case 0:
                return userName;
            case 1:
                return totalTime;
            default:
                throw new ArrayIndexOutOfBoundsException(columnNumber);
        }
    }

    @Override
    public String getColumnName(int columnNumber)
    {
        switch (columnNumber)
        {
            case 0:
                return "Consultant";
            case 1:
                return "Total Time";
            default:
                throw new ArrayIndexOutOfBoundsException(columnNumber);
        }
    }

}
