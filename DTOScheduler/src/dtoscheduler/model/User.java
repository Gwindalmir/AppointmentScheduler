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

/**
 *
 * @author Daniel
 */
public class User implements dtoscheduler.interfaces.IUpdatableDatabaseEntity
{
    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    private boolean updated;

    public User()
    {
        createDate = lastUpdate = ZonedDateTime.now();
        createdBy = lastUpdateBy = GlobalInformation.getInstance().getUser();
        active = true;
        userName = "";
    }

    public User(int userId)
    {
        this();
        this.userId = userId;
    }

    public User(User user)
    {
        this.userId = user.userId;
        this.userName = user.userName;
        this.password = user.password;
        this.active = user.active;
        this.createDate = user.createDate;
        this.createdBy = user.createdBy;
        this.lastUpdate = user.lastUpdate;
        this.lastUpdateBy = user.lastUpdateBy;
    }

    public User(int userId, String userName, String password, boolean active, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdateBy)
    {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        if (this.userId != userId)
            updated = true;

        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        if ((this.userName == null && userName != null) || !this.userName.equals(userName))
            updated = true;

        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        if ((this.password == null && password != null) || !this.password.equals(password))
            updated = true;

        this.password = password;
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
        if (!this.createDate.equals(this.createDate))
            updated = true;

        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        if (!this.createdBy.equals(this.createdBy))
            updated = true;

        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate)
    {
        if (!this.lastUpdate.equals(this.lastUpdate))
            updated = true;

        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy)
    {
        if (!this.lastUpdateBy.equals(this.lastUpdateBy))
            updated = true;

        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public void update() throws SQLException
    {
        if (userId == 0)
            add();

        if (updated)
        {
            Connection connection = SQLHelper.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE user SET userName = ?, password = ?, active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE userId = ?"))
            {
                stmt.setString(1, userName);
                stmt.setString(2, password);
                stmt.setShort(3, (short) (active ? 1 : 0));
                stmt.setTimestamp(4, java.sql.Timestamp.from(lastUpdate.toInstant()));
                stmt.setString(5, lastUpdateBy);
                stmt.setInt(6, userId);
                stmt.executeUpdate();
            }

            markDirty();
            refresh();
        }
    }

    @Override
    public void add() throws SQLException
    {
        Connection connection = SQLHelper.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO user (userName, password, active, lastUpdate, lastUpdateBy, createDate, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?)"))
        {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.setShort(3, (short) (active ? 1 : 0));
            stmt.setTimestamp(4, java.sql.Timestamp.from(lastUpdate.toInstant()));
            stmt.setString(5, lastUpdateBy);
            stmt.setTimestamp(6, java.sql.Timestamp.from(createDate.toInstant()));
            stmt.setString(7, createdBy);
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
                "DELETE FROM user WHERE userId = ?"))
        {
            stmt.setInt(1, userId);
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
                userId > 0 ?
                        "SELECT * FROM user WHERE userId = ?" :
                        "SELECT * FROM user WHERE userName = ?"))
        {
            if (userId > 0)
                stmt.setInt(1, userId);
            else
                stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            rs.next();

            userId = rs.getInt("userId");
            userName = rs.getString("userName");
            active = (rs.getShort("active") > 0);
            createDate = rs.getTimestamp("createDate").toInstant().atZone(ZoneId.of("UTC"));
            createdBy = rs.getString("createdBy");
            lastUpdate = rs.getTimestamp("lastUpdate").toInstant().atZone(ZoneId.of("UTC"));
            lastUpdateBy = rs.getString("lastUpdateBy");
        }

        updated = false;
    }

    public static User lookup(String username)
    {
        User user = new User();
        user.setUserName(username);
        if (user.lookup())
            return user;
        else
            return null;
    }

    public static User lookup(int userId)
    {
        User user = new User();
        user.userId = userId;
        if (user.lookup())
            return user;
        else
            return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof User)
        {
            User user = (User) obj;
            return user.userId == this.userId &&
                    (user.userName == null && this.userName == null || user.userName.equals(this.userName)) &&
                    (user.password == null && this.password == null || user.password.equals(this.password)) &&
                    user.active == this.active &&
                    user.lastUpdateBy.equals(this.lastUpdateBy) &&
                    user.lastUpdate.equals(this.lastUpdate) &&
                    user.createdBy.equals(this.createdBy) &&
                    user.createDate.equals(this.createDate);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + this.userId;
        hash = 89 * hash + Objects.hashCode(this.userName);
        hash = 89 * hash + Objects.hashCode(this.password);
        hash = 89 * hash + (this.active ? 1 : 0);
        hash = 89 * hash + Objects.hashCode(this.createDate);
        hash = 89 * hash + Objects.hashCode(this.createdBy);
        hash = 89 * hash + Objects.hashCode(this.lastUpdate);
        hash = 89 * hash + Objects.hashCode(this.lastUpdateBy);
        return hash;
    }
}
