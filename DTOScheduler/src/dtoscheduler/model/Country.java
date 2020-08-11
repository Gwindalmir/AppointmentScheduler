/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Country implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{
    private int countryId;
    private String country;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private boolean updated;

    public Country()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
        country = "";
    }

    public Country(int countryId)
    {
        this();
        this.countryId = countryId;
    }

    public Country(Country country)
    {
        this.countryId = country.countryId;
        this.country = country.country;
        this.createDate = country.createDate;
        this.createdBy = country.createdBy;
        this.lastUpdate = country.lastUpdate;
        this.lastUpdateBy = country.lastUpdateBy;
    }

    public Country(int countryId, String country, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCountryId()
    {
        return countryId;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        if (!country.equals(this.country))
            updated = true;

        this.country = country;
    }

    public ZonedDateTime getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate)
    {
        if (!createDate.equals(this.createDate))
            updated = true;

        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        if (!createdBy.equals(this.createdBy))
            updated = true;

        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate)
    {
        if (!lastUpdate.equals(this.lastUpdate))
            updated = true;

        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy)
    {
        if (!lastUpdateBy.equals(this.lastUpdateBy))
            updated = true;

        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public void update() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Updating '%s' %d", country, countryId));

        if (countryId == 0)
            add();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE country SET country = ?, lastUpdate = ?, lastUpdateBy = ? WHERE countryId = ?"))
            {
                stmt.setString(1, country);
                stmt.setTimestamp(2, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(3, lastUpdateBy);
                stmt.setInt(4, countryId);
                stmt.executeUpdate();
            }

            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        if(countryId != 0)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Entry already exists '%s'", country));
            return;
        }
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Inserting '%s'", country));

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO country (country, lastUpdate, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?)"))
        {
            stmt.setString(1, country);
            stmt.setTimestamp(2, java.sql.Timestamp.from(lastUpdate.toInstant()));
            stmt.setString(3, lastUpdateBy);
            stmt.setTimestamp(4, java.sql.Timestamp.from(createDate.toInstant()));
            stmt.setString(5, createdBy);
            stmt.executeUpdate();
        }
        markDirty();
        refresh();
    }

    @Override
    public void delete() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Deleting '%s' %d", country, countryId));

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM country WHERE countryId = ?"))
        {
            stmt.setInt(1, countryId);
            stmt.executeUpdate();
        }
        markDirty();
    }

    @Override
    public void refresh() throws SQLException
    {
        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                countryId > 0 ?
                        "SELECT * FROM country WHERE countryId = ?" :
                        "SELECT * FROM country WHERE country = ?"))
        {
            if (countryId > 0)
                stmt.setInt(1, countryId);
            else
                stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            countryId = rs.getInt("countryId");
            country = rs.getString("country");
            createDate = rs.getTimestamp("createDate").toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = rs.getTimestamp("lastUpdate").toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");
        }

        updated = false;
    }

    public static Country lookup(String country)
    {
        Country cty = new Country();
        cty.setCountry(country);
        if (cty.lookup())
            return cty;
        else
            return null;
    }

    public static Country lookup(int countryId)
    {
        Country cty = new Country();
        cty.countryId = countryId;
        if (cty.lookup())
            return cty;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Country)
        {
            Country country = (Country) obj;
            return country.countryId == this.countryId &&
                    country.country.equals(this.country) &&
                    country.lastUpdateBy.equals(this.lastUpdateBy) &&
                    country.lastUpdate.equals(this.lastUpdate) &&
                    country.createdBy.equals(this.createdBy) &&
                    country.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 79 * hash + this.countryId;
        hash = 79 * hash + Objects.hashCode(this.country);
        hash = 79 * hash + Objects.hashCode(this.createDate);
        hash = 79 * hash + Objects.hashCode(this.createdBy);
        hash = 79 * hash + Objects.hashCode(this.lastUpdate);
        hash = 79 * hash + Objects.hashCode(this.lastUpdateBy);
        return hash;
    }

}
