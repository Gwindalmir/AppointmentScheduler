/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.singleton;

import dtoscheduler.interfaces.IDatabaseReportEntity;
import dtoscheduler.model.Address;
import dtoscheduler.model.User;
import dtoscheduler.model.Appointment;
import dtoscheduler.model.Customer;
import dtoscheduler.model.City;
import dtoscheduler.model.Country;
import dtoscheduler.model.ReportScheduleForConsultant;
import dtoscheduler.model.ReportTimeSpentByConsultant;
import dtoscheduler.model.ReportTypePerMonth;
import java.sql.*;
import java.time.Duration;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daniel
 */
public class SQLHelper
{
    private static final SQLHelper _instance;
    private Connection connection;

    // Alternative to static constructor, to make singleton work
    static
    {
        _instance = new SQLHelper();
    }

    // Make this a singleton
    private SQLHelper()
    {
    }

    public static SQLHelper getInstance()
    {
        return _instance;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    public void saveChanges()
    {
        try
        {
            SQLHelper.getInstance().getConnection().commit();
            GlobalInformation.getInstance().setDirty(false);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void undoChanges()
    {
        try
        {
            SQLHelper.getInstance().getConnection().rollback();
            GlobalInformation.getInstance().setDirty(false);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<Country> getCountries() throws SQLException
    {
        ObservableList<Country> countries = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT countryId FROM country");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Country country = new Country(rs.getInt("countryId"));
                country.lookup();
                countries.add(country);
            }
        }
        return countries;
    }

    public ObservableList<City> getCities() throws SQLException
    {
        ObservableList<City> cities = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT cityId FROM city");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                City city = new City(rs.getInt("cityId"));
                city.lookup();
                cities.add(city);
            }
        }
        return cities;
    }

    public ObservableList<Address> getAddresses() throws SQLException
    {
        ObservableList<Address> addresses = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT addressId FROM address");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Address address = new Address(rs.getInt("addressId"));
                address.lookup();
                addresses.add(address);
            }
        }
        return addresses;
    }

    public ObservableList<Customer> getCustomers() throws SQLException
    {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT customerId FROM customer");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Customer customer = new Customer(rs.getInt("customerId"));
                customer.lookup();
                customers.add(customer);
            }
        }
        return customers;
    }

    public ObservableList<Appointment> getAppointments() throws SQLException
    {
        return getAppointments(null);
    }

    public ObservableList<Appointment> getAppointments(String user) throws SQLException
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String statement = "SELECT appointmentId FROM appointment";

        if (user != null)
            statement += " WHERE userId = " + User.lookup(user).getUserId();

        try (PreparedStatement stmt = connection.prepareStatement(statement);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Appointment appointment = new Appointment(rs.getInt("appointmentId"));
                appointment.lookup();
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public ObservableList<User> getUsers() throws SQLException
    {
        ObservableList<User> users = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT userId FROM user");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                User user = new User(rs.getInt("userId"));
                user.lookup();
                users.add(user);
            }
        }
        return users;
    }

    public ObservableList<String> getAppointmentTypes() throws SQLException
    {
        ObservableList<String> types = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT type FROM appointment");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                types.add(rs.getString("type"));
            }
        }
        return types;
    }

    public ObservableList<IDatabaseReportEntity> getReportTypePerMonth() throws SQLException
    {
        ObservableList<IDatabaseReportEntity> report = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM REPORT_TYPE_PER_MONTH");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                ReportTypePerMonth entity = new ReportTypePerMonth();
                entity.setCount(rs.getInt("count"));
                entity.setType(rs.getString("type"));
                entity.setMonth(rs.getString("month"));
                report.add(entity);
            }
        }
        return report;
    }

    public ObservableList<IDatabaseReportEntity> getReportConsultantSchedule() throws SQLException
    {
        ObservableList<IDatabaseReportEntity> report = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM REPORT_SCHEDULE_FOR_CONSULTANT");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                ReportScheduleForConsultant entity = new ReportScheduleForConsultant();
                entity.setUserName(rs.getString("userName"));
                entity.setTitle(rs.getString("title"));
                entity.setCustomerName(rs.getString("customerName"));
                entity.setStart(rs.getTimestamp("start").toInstant().atZone(ZoneId.systemDefault()));
                entity.setEnd(rs.getTimestamp("end").toInstant().atZone(ZoneId.systemDefault()));
                report.add(entity);
            }
        }
        return report;
    }

    public ObservableList<IDatabaseReportEntity> getReportTotalTimeByConsultant() throws SQLException
    {
        ObservableList<IDatabaseReportEntity> report = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM REPORT_TIME_SPENT_BY_CONSULTANT");
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                ReportTimeSpentByConsultant entity = new ReportTimeSpentByConsultant();
                entity.setUserName(rs.getString("userName"));
                entity.setTotalTime(Duration.ofSeconds(rs.getLong("totalTimeSeconds")));
                report.add(entity);
            }
        }
        return report;
    }
}
