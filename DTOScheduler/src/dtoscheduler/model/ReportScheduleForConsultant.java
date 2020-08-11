/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

import java.time.ZonedDateTime;


/*
This object maps the result set from this VIEW:

CREATE OR REPLACE VIEW REPORT_SCHEDULE_FOR_CONSULTANT AS 
SELECT user.userName, appointment.title, customer.customerName, appointment.start, appointment.end
FROM user INNER JOIN appointment ON appointment.userId = user.userId 
          INNER JOIN customer ON appointment.customerId = customer.customerId
ORDER BY user.userName, appointment.start;

 */
/**
 *
 * @author Daniel
 */
public class ReportScheduleForConsultant implements dtoscheduler.interfaces.IDatabaseReportEntity
{
    private String userName;
    private String title;
    private String customerName;
    private ZonedDateTime start;
    private ZonedDateTime end;

    public ReportScheduleForConsultant()
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public ZonedDateTime getStart()
    {
        return start;
    }

    public void setStart(ZonedDateTime start)
    {
        this.start = start;
    }

    public ZonedDateTime getEnd()
    {
        return end;
    }

    public void setEnd(ZonedDateTime end)
    {
        this.end = end;
    }

    @Override
    public int getColumnCount()
    {
        return 5;
    }

    @Override
    public Class<?> getColumnType(int columnNumber)
    {
        switch (columnNumber)
        {
            case 0:
                return userName.getClass();
            case 1:
                return title.getClass();
            case 2:
                return customerName.getClass();
            case 3:
                return start.getClass();
            case 4:
                return end.getClass();
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
                return title;
            case 2:
                return customerName;
            case 3:
                return start;
            case 4:
                return end;
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
                return "Appointment";
            case 2:
                return "Customer";
            case 3:
                return "Starts";
            case 4:
                return "Ends";
            default:
                throw new ArrayIndexOutOfBoundsException(columnNumber);
        }
    }
}
