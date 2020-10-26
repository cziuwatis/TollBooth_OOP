package DAO;

import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andrej Gorochov D00218937
 */
public class MysqlVehiclesDao extends MysqlDao implements VehiclesDaoInterface
{

    @Override
    public Set<String> getAllRegistrations() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> registrations = new HashSet<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT registration FROM vehicles";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                registrations.add(rs.getString("registration"));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllRegistrations() " + e.getMessage());
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
                throw new DaoException("getAllRegistrations() " + e.getMessage());
            }
        }
        return registrations;
    }

}
