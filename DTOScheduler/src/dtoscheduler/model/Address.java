/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.model;

import dtoscheduler.interfaces.IUpdatableDatabaseEntity;
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
public class Address implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{
    private int addressId;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private City cityId;
    private boolean updated;

    public Address()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
        address = "";
        address2 = "";
        postalCode = "";
        phone = "";
    }

    public Address(int addressId)
    {
        this();
        this.addressId = addressId;
    }

    public Address(Address address)
    {
        this.addressId = address.addressId;
        this.address = address.address;
        this.address2 = address.address2;
        this.postalCode = address.postalCode;
        this.phone = address.phone;
        this.createDate = address.createDate;
        this.createdBy = address.createdBy;
        this.lastUpdate = address.lastUpdate;
        this.lastUpdateBy = address.lastUpdateBy;
        this.cityId = new City(address.cityId);
    }

    public Address(int addressId, String address, String address2, String postalCode, String phone, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAddressId()
    {
        return addressId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        if (!this.address.equals(address))
            updated = true;

        this.address = address;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        if (!this.address2.equals(address2))
            updated = true;

        this.address2 = address2;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        if (!this.postalCode.equals(postalCode))
            updated = true;

        this.postalCode = postalCode;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        if (!this.phone.equals(phone))
            updated = true;

        this.phone = phone;
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

    public City getCityId()
    {
        return cityId;
    }

    public void setCityId(City cityId)
    {
        if (this.cityId == null || !this.cityId.equals(cityId))
            updated = true;

        this.cityId = cityId;
    }

    @Override
    public void update() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Updating '%s' %d", address, addressId));

        if (addressId == 0)
            add();

        // Make sure we unconditionally update any dependencies first
        cityId.update();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?"))
            {
                stmt.setString(1, address);
                stmt.setString(2, address2);
                stmt.setInt(3, cityId.getCityId());
                stmt.setString(4, postalCode);
                stmt.setString(5, phone);
                stmt.setTimestamp(6, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(7, lastUpdateBy);
                stmt.setInt(8, addressId);
                stmt.executeUpdate();
            }
            
            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Inserting '%s'", address));

        // Make sure we add any dependencies first
        cityId.add();

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO address (address, address2, cityId, postalCode, phone, lastUpdate, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            stmt.setString(1, address);
            stmt.setString(2, address2);
            stmt.setInt(3, cityId.getCityId());
            stmt.setString(4, postalCode);
            stmt.setString(5, phone);
            stmt.setTimestamp(6, java.sql.Timestamp.from(lastUpdate.toInstant()));
            stmt.setString(7, lastUpdateBy);
            stmt.setTimestamp(8, java.sql.Timestamp.from(createDate.toInstant()));
            stmt.setString(9, createdBy);
            stmt.executeUpdate();
        }
        markDirty();
        refresh();
    }

    @Override
    public void delete() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Deleting '%s' %d", address, addressId));

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM address WHERE addressId = ?"))
        {
            stmt.setInt(1, addressId);
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
                addressId > 0 ?
                        "SELECT * FROM address WHERE addressId = ?" :
                        "SELECT * FROM address WHERE address = ?"))
        {
            if (addressId > 0)
                stmt.setInt(1, addressId);
            else
                stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            addressId = rs.getInt("addressId");
            address = rs.getString("address");
            postalCode = rs.getString("postalCode");
            phone = rs.getString("phone");
            cityId = City.lookup(rs.getInt("cityId"));
            createDate = rs.getTimestamp("createDate").toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = rs.getTimestamp("lastUpdate").toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");
        }

        updated = false;
    }

    public static Address lookup(String address)
    {
        Address addrs = new Address();
        addrs.setAddress(address);
        if (addrs.lookup())
            return addrs;
        else
            return null;
    }

    public static Address lookup(int addressId)
    {
        Address addrs = new Address();
        addrs.addressId = addressId;
        if (addrs.lookup())
            return addrs;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Address)
        {
            Address add = (Address) obj;
            return add.addressId == this.addressId &&
                    add.address.equals(this.address) &&
                    add.address2.equals(this.address2) &&
                    add.postalCode.equals(this.postalCode) &&
                    add.phone.equals(this.phone) &&
                    (add.cityId == null && this.cityId == null || add.cityId.equals(this.cityId)) &&
                    add.lastUpdateBy.equals(this.lastUpdateBy) &&
                    add.lastUpdate.equals(this.lastUpdate) &&
                    add.createdBy.equals(this.createdBy) &&
                    add.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 61 * hash + this.addressId;
        hash = 61 * hash + Objects.hashCode(this.address);
        hash = 61 * hash + Objects.hashCode(this.address2);
        hash = 61 * hash + Objects.hashCode(this.postalCode);
        hash = 61 * hash + Objects.hashCode(this.phone);
        hash = 61 * hash + Objects.hashCode(this.createDate);
        hash = 61 * hash + Objects.hashCode(this.createdBy);
        hash = 61 * hash + Objects.hashCode(this.lastUpdate);
        hash = 61 * hash + Objects.hashCode(this.lastUpdateBy);
        hash = 61 * hash + Objects.hashCode(this.cityId);
        return hash;
    }

}
