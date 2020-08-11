/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class City implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{
    private int cityId;
    private String city;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private Country countryId;
    private boolean updated;

    public City()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
        city = "";
    }

    public City(int cityId)
    {
        this();
        this.cityId = cityId;
    }

    public City(City city)
    {
        this.cityId = city.cityId;
        this.city = city.city;
        this.createDate = city.createDate;
        this.createdBy = city.createdBy;
        this.lastUpdate = city.lastUpdate;
        this.lastUpdateBy = city.lastUpdateBy;
        this.countryId = new Country(city.countryId);
    }

    public City(int cityId, String city, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this.cityId = cityId;
        this.city = city;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCityId()
    {
        return cityId;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        if (!this.city.equals(city))
            updated = true;

        this.city = city;
    }

    public ZonedDateTime getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate)
    {
        if (!this.createDate.equals(createDate))
            updated = true;

        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        if (!this.createdBy.equals(createdBy))
            updated = true;

        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate)
    {
        if (!this.lastUpdate.equals(lastUpdate))
            updated = true;

        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy)
    {
        if (!this.lastUpdateBy.equals(lastUpdateBy))
            updated = true;

        this.lastUpdateBy = lastUpdateBy;
    }

    public Country getCountryId()
    {
        return countryId;
    }

    public void setCountryId(Country countryId)
    {
        if (this.countryId == null || !this.countryId.equals(countryId))
            updated = true;

        this.countryId = countryId;
    }

    @Override
    public void update() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Updating '%s' %d", city, cityId));

        if (cityId == 0)
            add();

        // Make sure we unconditionally update any dependencies first
        countryId.update();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE city SET city = ?, countryId = ?, lastUpdate = ?, lastUpdateBy = ? WHERE cityId = ?"))
            {
                stmt.setString(1, city);
                stmt.setInt(2, countryId.getCountryId());
                stmt.setTimestamp(3, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(4, lastUpdateBy);
                stmt.setInt(5, cityId);
                stmt.executeUpdate();
            }
            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        if(cityId != 0)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Entry already exists '%s'", city));
            return;
        }
        
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Inserting '%s'", city));

        // Make sure we add any dependencies first
        countryId.add();

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO city (city, countryId, lastUpdate, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?, ?)"))
        {
            stmt.setString(1, city);
            stmt.setInt(2, countryId.getCountryId());
            stmt.setTimestamp(3, java.sql.Timestamp.from(lastUpdate.toInstant()));
            stmt.setString(4, lastUpdateBy);
            stmt.setTimestamp(5, java.sql.Timestamp.from(createDate.toInstant()));
            stmt.setString(6, createdBy);
            stmt.executeUpdate();
        }
        markDirty();
        refresh();
    }

    @Override
    public void delete() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Deleting '%s' %d", city, cityId));

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM city WHERE cityId = ?"))
        {
            stmt.setInt(1, cityId);
            stmt.executeUpdate();
        }

        // Note, dependencies are not being deleted
        markDirty();
    }

    @Override
    public void refresh() throws SQLException
    {
        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                cityId > 0 ?
                        "SELECT * FROM city WHERE cityId = ?" :
                        "SELECT * FROM city WHERE city = ?"))
        {
            if (cityId > 0)
                stmt.setInt(1, cityId);
            else
                stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            cityId = rs.getInt("cityId");
            city = rs.getString("city");
            countryId = Country.lookup(rs.getInt("countryId"));
            createDate = rs.getTimestamp("createDate").toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = rs.getTimestamp("lastUpdate").toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");
        }

        updated = false;
    }

    public static City lookup(String city)
    {
        City cty = new City();
        cty.setCity(city);
        if (cty.lookup())
            return cty;
        else
            return null;
    }

    public static City lookup(int cityId)
    {
        City cty = new City();
        cty.cityId = cityId;
        if (cty.lookup())
            return cty;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof City)
        {
            City city = (City) obj;
            return city.cityId == this.cityId &&
                    city.city.equals(this.city) &&
                    (city.countryId == null && this.countryId == null || city.countryId.equals(this.countryId)) &&
                    city.lastUpdateBy.equals(this.lastUpdateBy) &&
                    city.lastUpdate.equals(this.lastUpdate) &&
                    city.createdBy.equals(this.createdBy) &&
                    city.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.cityId;
        hash = 97 * hash + Objects.hashCode(this.city);
        hash = 97 * hash + Objects.hashCode(this.createDate);
        hash = 97 * hash + Objects.hashCode(this.createdBy);
        hash = 97 * hash + Objects.hashCode(this.lastUpdate);
        hash = 97 * hash + Objects.hashCode(this.lastUpdateBy);
        hash = 97 * hash + Objects.hashCode(this.countryId);
        return hash;
    }
}
