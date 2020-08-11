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

/**
 *
 * @author Daniel
 */
public class Appointment implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private Customer customerId;
    private User userId;
    private boolean updated;

    public Appointment()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
    }

    public Appointment(int appointmentId)
    {
        this();
        this.appointmentId = appointmentId;
    }

    public Appointment(Appointment appointment)
    {
        this.appointmentId = appointment.appointmentId;
        this.customerId = new Customer(appointment.customerId);
        this.userId = new User(appointment.userId);
        this.title = appointment.title;
        this.description = appointment.description;
        this.location = appointment.location;
        this.contact = appointment.contact;
        this.type = appointment.type;
        this.url = appointment.url;
        this.start = appointment.start;
        this.end = appointment.end;
        this.createDate = appointment.createDate;
        this.createdBy = appointment.createdBy;
        this.lastUpdate = appointment.lastUpdate;
        this.lastUpdateBy = appointment.lastUpdateBy;
    }

    public Appointment(int appointmentId, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, ZonedDateTime end, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAppointmentId()
    {
        return appointmentId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        if (this.title == null || !this.title.equals(title))
            updated = true;
        
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        if (this.description == null || !this.description.equals(description))
            updated = true;
        
        this.description = description;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        if (this.location == null || !this.location.equals(location))
            updated = true;
        
        this.location = location;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        if (this.contact == null || !this.contact.equals(contact))
            updated = true;
        
        this.contact = contact;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        if (this.type == null || !this.type.equals(type))
            updated = true;
        
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        if (this.url == null || !this.url.equals(url))
            updated = true;
        
        this.url = url;
    }

    public ZonedDateTime getStart()
    {
        return start;
    }

    public void setStart(ZonedDateTime start)
    {
        if (this.start == null || !this.start.equals(start))
            updated = true;
        
        this.start = start;
    }

    public ZonedDateTime getEnd()
    {
        return end;
    }

    public void setEnd(ZonedDateTime end)
    {
        if (this.end == null || !this.end.equals(end))
            updated = true;
        
        this.end = end;
    }

    public ZonedDateTime getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate)
    {
        if (this.createDate == null || !this.createDate.equals(createDate))
            updated = true;
        
        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        if (this.createdBy == null || !this.createdBy.equals(createdBy))
            updated = true;
        
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate)
    {
        if (this.lastUpdate == null || !this.lastUpdate.equals(lastUpdate))
            updated = true;
        
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy)
    {
        if (this.lastUpdateBy == null || !this.lastUpdateBy.equals(lastUpdateBy))
            updated = true;
        
        this.lastUpdateBy = lastUpdateBy;
    }

    public Customer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Customer customerId)
    {
        if (this.customerId == null || !this.customerId.equals(customerId))
            updated = true;
        
        this.customerId = customerId;
    }

    public User getUserId()
    {
        return userId;
    }

    public void setUserId(User userId)
    {
        if (this.userId == null || !this.userId.equals(userId))
            updated = true;
        
        this.userId = userId;
    }

    @Override
    public void update() throws SQLException
    {
        if (appointmentId == 0)
            add();

        // Make sure we unconditionally update any dependencies first
        customerId.update();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?"))
            {
                stmt.setInt(1, customerId.getCustomerId());
                stmt.setInt(2, userId.getUserId());
                stmt.setString(3, title);
                stmt.setString(4, description);
                stmt.setString(5, location);
                stmt.setString(6, contact);
                stmt.setString(7, type);
                stmt.setString(8, url);
                stmt.setTimestamp(9, java.sql.Timestamp.from(start.toInstant()));
                stmt.setTimestamp(10, java.sql.Timestamp.from(end.toInstant()));
                stmt.setTimestamp(11, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(12, lastUpdateBy);
                stmt.setInt(13, appointmentId);
                stmt.executeUpdate();
            }
            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        // Make sure we add any dependencies first
        customerId.add();

        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, lastUpdate, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            stmt.setInt(1, customerId.getCustomerId());
            stmt.setInt(2, userId.getUserId());
            stmt.setString(3, title);
            stmt.setString(4, description);
            stmt.setString(5, location);
            stmt.setString(6, contact);
            stmt.setString(7, type);
            stmt.setString(8, url);
            stmt.setTimestamp(9, java.sql.Timestamp.from(start.toInstant()));   // Store in UTC
            stmt.setTimestamp(10, java.sql.Timestamp.from(end.toInstant()));    // Store in UTC
            stmt.setTimestamp(11, java.sql.Timestamp.from(lastUpdate.toInstant()));
            stmt.setString(12, lastUpdateBy);
            stmt.setTimestamp(13, java.sql.Timestamp.from(createDate.toInstant()));
            stmt.setString(14, createdBy);
            stmt.executeUpdate();
        }
        
        markDirty();
        refresh();
    }

    @Override
    public void delete() throws SQLException
    {
        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM appointment WHERE appointmentId = ?"))
        {
            stmt.setInt(1, appointmentId);
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
                appointmentId > 0 ?
                        "SELECT * FROM appointment WHERE appointmentId = ?" :
                        "SELECT * FROM appointment WHERE title = ?"))
        {
            if (appointmentId > 0)
                stmt.setInt(1, appointmentId);
            else
                stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            customerId = Customer.lookup(rs.getInt("customerId"));
            userId = User.lookup(rs.getInt("userId"));
            title = rs.getString("title");
            description = rs.getString("description");
            location = rs.getString("location");
            contact = rs.getString("contact");
            type = rs.getString("type");
            url = rs.getString("url");
            start = rs.getTimestamp("start").toInstant().atZone(ZoneId.systemDefault());    // Fetch and convert to users local time
            end = rs.getTimestamp("end").toInstant().atZone(ZoneId.systemDefault());        // Fetch and convert to users local time
            createDate = rs.getTimestamp("createDate").toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = rs.getTimestamp("lastUpdate").toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");

            // At this point, the appointment times have been automatically adjusted to the users local time zone.
            // This satisifies Rubric Task E
        }

        updated = false;
    }

    public static Appointment lookup(String title)
    {
        Appointment appointment = new Appointment();
        appointment.setTitle(title);
        if (appointment.lookup())
            return appointment;
        else
            return null;
    }

    public static Appointment lookup(int appointmentId)
    {
        Appointment appointment = new Appointment();
        appointment.appointmentId = appointmentId;
        if (appointment.lookup())
            return appointment;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Appointment)
        {
            Appointment app = (Appointment) obj;
            return app.appointmentId == this.appointmentId &&
                    (app.customerId == null && this.customerId == null || app.customerId.equals(this.customerId)) &&
                    (app.contact == null && this.contact == null || app.contact.equals(this.contact)) &&
                    (app.location == null && this.location == null || app.location.equals(this.location)) &&
                    (app.description == null && this.description == null || app.description.equals(this.description)) &&
                    (app.url == null && this.url == null || app.url.equals(this.url)) &&
                    (app.title == null && this.title == null || app.title.equals(this.title)) &&
                    (app.start == null && this.start == null || app.start.equals(this.start)) &&
                    (app.end == null && this.end == null || app.end.equals(this.end)) &&
                    (app.userId == null && this.userId == null || app.userId.equals(this.userId)) &&
                    (app.type == null && this.type == null || app.type.equals(this.type)) &&
                    app.lastUpdateBy.equals(this.lastUpdateBy) &&
                    app.lastUpdate.equals(this.lastUpdate) &&
                    app.createdBy.equals(this.createdBy) &&
                    app.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + this.appointmentId;
        hash = 17 * hash + Objects.hashCode(this.title);
        hash = 17 * hash + Objects.hashCode(this.description);
        hash = 17 * hash + Objects.hashCode(this.location);
        hash = 17 * hash + Objects.hashCode(this.contact);
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.url);
        hash = 17 * hash + Objects.hashCode(this.start);
        hash = 17 * hash + Objects.hashCode(this.end);
        hash = 17 * hash + Objects.hashCode(this.createDate);
        hash = 17 * hash + Objects.hashCode(this.createdBy);
        hash = 17 * hash + Objects.hashCode(this.lastUpdate);
        hash = 17 * hash + Objects.hashCode(this.lastUpdateBy);
        hash = 17 * hash + Objects.hashCode(this.customerId);
        hash = 17 * hash + Objects.hashCode(this.userId);
        return hash;
    }
}
