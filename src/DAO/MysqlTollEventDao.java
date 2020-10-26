package DAO;

import DTO.TollEvent;
import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.TollEventsMap;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class MysqlTollEventDao extends MysqlDao implements TollEventDaoInterface
{

    @Override
    public boolean writeToDatabase(TollEventsMap tollEvents) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            for (String key : tollEvents.keySet())
            {
                String registrationNumber = key;
                query = "SELECT id FROM vehicles WHERE registration = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, registrationNumber);
                rs = ps.executeQuery();
                if (rs.next())
                {
                    long registrationId = rs.getLong("id");
                    query = "INSERT INTO tollEvents VALUES ";
                    for (TollEvent tEvent : tollEvents.get(key))
                    {
                        query += "(" + registrationId + ",\"" + tEvent.getPhotoId() + "\",\"" + tEvent.getTimestamp() + "\", 0),";
                    }
                    ps = con.prepareStatement(query.substring(0, query.length() - 1));
                    int result = ps.executeUpdate();
                    if (result > 0)
                    {
                        isSuccess = true;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("writeToDatabase() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("writeToDatabase() " + e.getMessage());
            }
        }
        return isSuccess;
    }

    @Override
    public List<TollEvent> getTollEventsByRegistration(String registration) throws DaoException
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT v.registration AS registration, t.photoId AS photoId, t.eventTimeStamp AS eventTimeStamp FROM vehicles v, tollEvents t WHERE v.id = t.registrationId AND v.registration = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, registration);
            rs = ps.executeQuery();
            while (rs.next())
            {
                tollEvents.add(TollEvent.createTollEvent(registration, rs.getLong("photoId"), rs.getTimestamp("eventTimeStamp")));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventsByRegistration() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventsByRegistration() " + e.getMessage());
            }
        }
        return tollEvents;
    }

    @Override
    public Map<String, List<TollEvent>> getAllTollEvents() throws DaoException
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, List<TollEvent>> tollEventsMap = new HashMap<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT v.registration AS registration, t.photoId AS photoId, t.eventTimeStamp AS eventTimeStamp FROM vehicles v, tollEvents t WHERE v.id = t.registrationId";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next())
            {
                List<TollEvent> tollEvents = tollEventsMap.get(rs.getString("registration"));
                if (tollEvents == null)
                {
                    tollEvents = new ArrayList<>();
                }
                tollEvents.add(TollEvent.createTollEvent(rs.getString("registration"), rs.getLong("photoId"), rs.getTimestamp("eventTimeSTamp")));
                tollEventsMap.put(rs.getString("registration"), tollEvents);
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventsByRegistration() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventsByRegistration() " + e.getMessage());
            }
        }
        return tollEventsMap;
    }

    @Override
    public List<TollEvent> getTollEventDetails() throws DaoException
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT v.registration AS registration, t.photoId AS photoId, t.eventTimeStamp AS eventTimeStamp FROM tollEvents t, vehicles v WHERE v.id = t.registrationId ORDER BY v.registration, t.photoId";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next())
            {
                tollEvents.add(TollEvent.createTollEvent(rs.getString("registration"), rs.getLong("photoId"), rs.getTimestamp("eventTimeStamp")));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventDetails() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventDetails() " + e.getMessage());
            }
        }
        return tollEvents;
    }

    @Override
    public List<String> getAllRegistrationsThroughToll() throws DaoException
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> registrations = new ArrayList<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT DISTINCT v.registration AS registration FROM tollEvents t, vehicles v WHERE t.registrationId = v.id ORDER BY v.registration";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next())
            {
                registrations.add(rs.getString("registration"));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventDetails() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventDetails() " + e.getMessage());
            }
        }
        return registrations;
    }

    @Override
    public List<TollEvent> getTollEventsSinceDateTime(LocalDateTime date) throws DaoException
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (date == null)
        {
            throw new IllegalArgumentException("Date cannot be null");
        }
        List<TollEvent> tollEvents = new ArrayList<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT v.registration AS registration, t.photoId AS photoId, t.eventTimeStamp AS eventTimeStamp "
                    + "FROM tollEvents t, vehicles v "
                    + "WHERE v.id = t.registrationId "
                    + "AND eventTimeStamp > ? "
                    + "ORDER BY t.eventTimeStamp";
            ps = con.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(date));
            rs = ps.executeQuery();
            while (rs.next())
            {
                tollEvents.add(TollEvent.createTollEvent(rs.getString("registration"), rs.getLong("photoId"), rs.getTimestamp("eventTimeStamp")));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventDetails() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventDetails() " + e.getMessage());
            }
        }
        return tollEvents;
    }

    @Override
    public List<TollEvent> getTollEventsBetweenDateTime(LocalDateTime startDate, LocalDateTime endDate) throws DaoException
    {

        if (startDate == null)
        {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        else if (endDate == null)
        {
            throw new IllegalArgumentException("endDate cannot be null");

        }
        else if (startDate.isAfter(endDate))
        {
            throw new IllegalArgumentException("startDate cannot appear after endDate");
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        String query;
        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            query = "SELECT v.registration AS registration, t.photoId AS photoId, t.eventTimeStamp AS eventTimeStamp "
                    + "FROM tollEvents t, vehicles v "
                    + "WHERE v.id = t.registrationId "
                    + "AND eventTimeStamp > ? "
                    + "AND eventTimeStamp < ? "
                    + "ORDER BY t.eventTimeStamp";
            ps = con.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            rs = ps.executeQuery();
            while (rs.next())
            {
                tollEvents.add(TollEvent.createTollEvent(rs.getString("registration"), rs.getLong("photoId"), rs.getTimestamp("eventTimeStamp")));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getTollEventDetails() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getTollEventDetails() " + e.getMessage());
            }
        }
        return tollEvents;
    }

}
