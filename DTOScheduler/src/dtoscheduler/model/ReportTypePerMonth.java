/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

/*
This object maps the result set from this VIEW:

CREATE VIEW REPORT_TYPE_PER_MONTH AS 
SELECT COUNT(appointmentId) AS count, type, DATE_FORMAT(start,'%Y-%m') AS month
FROM appointment
GROUP BY month, type
ORDER BY month DESC, type;

 */
/**
 *
 * @author Daniel
 */
public class ReportTypePerMonth implements dtoscheduler.interfaces.IDatabaseReportEntity
{
    private long count;
    private String type;
    private String month;

    public ReportTypePerMonth()
    {
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    @Override
    public int getColumnCount()
    {
        return 3;
    }

    @Override
    public Class<?> getColumnType(int columnNumber)
    {
        switch (columnNumber)
        {
            case 0:
                return ((Object) count).getClass();
            case 1:
                return type.getClass();
            case 2:
                return month.getClass();
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
                return count;
            case 1:
                return type;
            case 2:
                return month;
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
                return "Appointments";
            case 1:
                return "Type";
            case 2:
                return "Month";
            default:
                throw new ArrayIndexOutOfBoundsException(columnNumber);
        }
    }
}
