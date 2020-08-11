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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Customer implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{
    private int customerId;
    private String customerName;
    private boolean active;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private Address addressId;
    private boolean updated;

    public Customer()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
        active = true;
        customerName = "";
    }

    public Customer(int customerId)
    {
        this();
        this.customerId = customerId;
    }

    public Customer(Customer customer)
    {
        this();
        if(customer != null)
        {
            this.customerId = customer.customerId;
            this.active = customer.active;
            this.addressId = new Address(customer.addressId);
            this.createDate = customer.createDate;
            this.createdBy = customer.createdBy;
            this.customerName = customer.customerName;
            this.lastUpdate = customer.lastUpdate;
            this.lastUpdateBy = customer.lastUpdateBy;
        }
    }

    public Customer(int customerId, String customerName, boolean active, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this();
        
        this.customerId = customerId;
        this.customerName = customerName;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        if (this.customerName == null || !this.customerName.equals(customerName))
            updated = true;

        this.customerName = customerName;
    }

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        if (this.active != active)
            updated = true;

        this.active = active;
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

    public Address getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Address addressId)
    {
        if (this.addressId == null || !this.addressId.equals(addressId))
            updated = true;

        this.addressId = addressId;
    }

    @Override
    public void update() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Updating '%s' %d", customerName, customerId));

        if (customerId == 0)
            add();

        // Make sure we unconditionally update any dependencies first
        addressId.update();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE customer SET customerName = ?, active = ?, addressId = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?"))
            {
                stmt.setString(1, customerName);
                stmt.setBoolean(2, active);
                stmt.setInt(3, addressId.getAddressId());
                stmt.setTimestamp(4, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(5, lastUpdateBy);
                stmt.setInt(6, customerId);
                stmt.executeUpdate();
            }

            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        if (customerId == 0)
        {
            Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Inserting '%s'", customerName));

            // Make sure we add any dependencies first
            addressId.add();

            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO customer (customerName, active, addressId, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?, ?)"))
            {
                stmt.setString(1, customerName);
                stmt.setBoolean(2, active);
                stmt.setInt(3, addressId.getAddressId());
                stmt.setString(4, lastUpdateBy);
                stmt.setTimestamp(5, java.sql.Timestamp.from(createDate.toInstant()));
                stmt.setString(6, createdBy);
                stmt.executeUpdate();
            }
            markDirty();
            refresh();
        }
    }

    @Override
    public void delete() throws SQLException
    {
        Logger.getLogger(dtoscheduler.DTOScheduler.class.toString()).log(Level.INFO, String.format("Deleting '%s' %d", customerName, customerId));

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM customer WHERE customerId = ?"))
        {
            stmt.setInt(1, customerId);
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
                customerId > 0 ?
                        "SELECT * FROM customer WHERE customerId = ?" :
                        "SELECT * FROM customer WHERE customerName = ?"))
        {
            if (customerId > 0)
                stmt.setInt(1, customerId);
            else
                stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            customerId = rs.getInt("customerId");
            customerName = rs.getString("customerName");
            active = rs.getBoolean("active");
            addressId = Address.lookup(rs.getInt("addressId"));
            createDate = (rs.getTimestamp("createDate") != null ? rs.getTimestamp("createDate") : Timestamp.from(Instant.now())).toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = (rs.getTimestamp("lastUpdate") != null ? rs.getTimestamp("lastUpdate") : Timestamp.from(Instant.now())).toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");
        }

        updated = false;
    }

    public static Customer lookup(String customerName)
    {
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        if (customer.lookup())
            return customer;
        else
            return null;
    }

    public static Customer lookup(int customerId)
    {
        Customer customer = new Customer();
        customer.customerId = customerId;
        if (customer.lookup())
            return customer;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Customer)
        {
            Customer cus = (Customer) obj;
            return cus.customerId == this.customerId &&
                    cus.customerName.equals(this.customerName) &&
                    cus.active == this.active &&
                    (cus.addressId == null && this.addressId == null || cus.addressId.equals(this.addressId)) &&
                    cus.lastUpdateBy.equals(this.lastUpdateBy) &&
                    cus.lastUpdate.equals(this.lastUpdate) &&
                    cus.createdBy.equals(this.createdBy) &&
                    cus.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 61 * hash + this.customerId;
        hash = 61 * hash + Objects.hashCode(this.customerName);
        hash = 61 * hash + (this.active ? 1 : 0);
        hash = 61 * hash + Objects.hashCode(this.createDate);
        hash = 61 * hash + Objects.hashCode(this.createdBy);
        hash = 61 * hash + Objects.hashCode(this.lastUpdate);
        hash = 61 * hash + Objects.hashCode(this.lastUpdateBy);
        hash = 61 * hash + Objects.hashCode(this.addressId);
        return hash;
    }
}
